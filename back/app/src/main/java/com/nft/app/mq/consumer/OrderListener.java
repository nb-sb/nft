package com.nft.app.mq.consumer;

import cn.hutool.json.JSONUtil;
import com.nft.app.mq.producer.OrderPublisher;
import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.Constants.RedisKey;
import com.nft.common.Constants.ResponseCode;
import com.nft.common.Constants.payOrderStatus;
import com.nft.common.ElasticSearch.ElasticSearchUtils;
import com.nft.common.ElasticSearch.UserOrderSimpleES;
import com.nft.common.Rabbitmq.RabbitMqConstant;
import com.nft.common.Redis.RedisConstant;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Utils.OrderNumberUtil;
import com.nft.domain.detail.IDetailInfoRespository;
import com.nft.domain.nft.model.entity.SellInfoEntity;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.DetailInfoVo;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.domain.nft.service.INftInfoService;
import com.nft.domain.order.model.entity.OrderEntity;
import com.nft.domain.order.model.req.AddOrderMqMessage;
import com.nft.domain.order.respository.IOrderInfoRespository;
import com.nft.domain.order.service.OrderEntityFatory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fisco.bcos.sdk.utils.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 监听队列
 */
@Component
@Log4j2
@AllArgsConstructor
public class OrderListener {
    private final IOrderInfoRespository iOrderInfoRespository;
    private final ISellInfoRespository iSellInfoRespository;
    private final ElasticSearchUtils elasticSearchUtils;
    private final RedisUtil redisUtil;
    private final OrderPublisher orderPublisher;
    private final DistributedRedisLock distributedRedisLock;
    private final IDetailInfoRespository iDetailInfoRespository;
    private final OrderEntityFatory orderEntityFatory;
    private final INftInfoService iNftInfoService;


    /**
     * @Des 订单状态检查
     * @Date 2024/1/5 15:43
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMqConstant.DELAY_QUEUE, declare = "true"),
            exchange = @Exchange(name = RabbitMqConstant.DELAY_DERECT, delayed = "true"),
            key = RabbitMqConstant.ORDER_CHECK_STATUS ))
    public void ORDER_CHECK_STATUS(String orderId) {
        log.info("消息接受成功 orderId : "+orderId);
        //检查订单状态 如果未支付则取消，其他状态如取消订单等状态则不修改
        OrderEntity orderEntity = iOrderInfoRespository.selectOrderInfoByNumber(orderId);
        Integer orderStatus = orderEntity.getStatus();
        if (!payOrderStatus.NO_PAY.equals(orderStatus)) {
            //订单状态只能是未支付的，但是此时订单状态未：已支付/已取消
            log.info("订单状态无需修改 orderId : "+orderId +" status ： " + orderStatus);
            return;
        }
        //修改订单状态为已取消
        orderEntity.setStatus(payOrderStatus.CANCEL);
        boolean b = iOrderInfoRespository.save(orderEntity);
        // 退回订单剩余的数量
        SellInfoEntity sellInfoEntity = iSellInfoRespository.selectSellInfoById(orderEntity.getProductId());
        sellInfoEntity.setRemain(sellInfoEntity.getRemain() + 1);
        boolean b1 = iSellInfoRespository.setSellStocks(sellInfoEntity.getId(), sellInfoEntity.getRemain());

        if (!b) {
            log.error("修改订单状态为已取消 失败");
            throw new APIException(ResponseCode.NO_UPDATE, "修改订单状态为已取消 失败");
        }
        if (!b1) {
            log.error("退回订单剩余的数量 失败");
            throw new APIException(ResponseCode.NO_UPDATE, "退回订单剩余的数量 失败");
        } else {
            String key = RedisKey.REDIS_COLLECTION(orderEntity.getProductId());
            distributedRedisLock.acquireReadLock(key);
            try {
                //2）更新redis中的商品信息
                String conllectionCacheKey = redisUtil.getStr(key);
                if (!StringUtils.isEmpty(conllectionCacheKey)) {
                    //如果是空缓存的话 //这种原因一般是藏品已经删除
                    distributedRedisLock.acquireWriteLock(key);
                    try {
                        //更新藏品库存
                        if (!Constants.RedisKey.REDIS_EMPTY_CACHE().equals(conllectionCacheKey)) {
                            ConllectionInfoVo bean = JSONUtil.toBean(conllectionCacheKey, ConllectionInfoVo.class);
                            redisUtil.set(key, bean.getRemain(), RedisConstant.MINUTE_30);
                        }
                    }finally {
                        distributedRedisLock.releaseWriteLock(key);
                    }
                }
            }finally {
                distributedRedisLock.releaseReadLock(key);
            }

        }
    }
    //接受mq请求，添加订单数据
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMqConstant.ADD_ORDER_QUEUE, declare = "true"),
            exchange = @Exchange(name = RabbitMqConstant.ADD_ORDER_DERECT, type = ExchangeTypes.DIRECT),
            key = RabbitMqConstant.ADD_ORDER_KEY
    ))
    public void addOrder(String jsonStr) {
        log.info("消息接受成功add Order: "+jsonStr);
        AddOrderMqMessage bean = JSONUtil.toBean(jsonStr, AddOrderMqMessage.class);
        Integer userid = bean.getUserId();
        Date time = bean.getTime();
        Integer conllectionId = bean.getConllectionId();
        ConllectionInfoVo conllectionInfoVo = iNftInfoService.selectByCollectId(conllectionId);
        // 4.将藏品添加至订单中
        String orderNo = OrderNumberUtil.generateOrderNumber(userid, conllectionId);
        OrderEntity orderEntity = orderEntityFatory.newInstance(conllectionId,conllectionInfoVo.getPath(), conllectionInfoVo.getName(),
                conllectionInfoVo.getPrice(),conllectionInfoVo.getPrice());
        orderEntity.initOrder(userid,time);
        boolean b = iOrderInfoRespository.creat(orderEntity);
        if (!b) {
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "藏品添加至订单失败");
        }
        //1)decrRemain
//        //2）更新redis中的商品信息 // 这里实际上可以直接将缓存给删掉，等下次查询的时候自动更新最新的，无需修改
//        redisUtil.del(Constants.RedisKey.REDIS_COLLECTION(conllectionId));
        // 5.发送mq请求，半小时后检查订单状态
        orderPublisher.SendCheckMessage(orderNo);
        //添加订单信息至es中
        UserOrderSimpleES userOrderSimpleES = new UserOrderSimpleES()
                .setUserId(userid)
                .setOrderNo(orderNo)
                .setProductPrice(conllectionInfoVo.getPrice())
                .setProductImg(conllectionInfoVo.getPath())
                .setProductName(conllectionInfoVo.getName())
                .setInitDate(time)
                .setStatus(Constants.payOrderStatus.NO_PAY);
        elasticSearchUtils.insert(userOrderSimpleES);
    }
    //接受mq请求，添加es的用户订单信息至这里
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMqConstant.ADDES_QUEUE, declare = "true"),
            exchange = @Exchange(name = RabbitMqConstant.ADDES_DERECT, type = ExchangeTypes.DIRECT),
            key = RabbitMqConstant.ADDES_KEY
    ))
    public void esadd(String jsonStr) {
        log.info("消息接受成功es add jsonStr : "+jsonStr);
        UserOrderSimpleES bean = JSONUtil.toBean(jsonStr, UserOrderSimpleES.class);
        //查询订单信息，并添加es
        elasticSearchUtils.insert(bean);
    }

    //添加流水表信息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMqConstant.ADD_DETAIL_QUEUE, declare = "true"),
            exchange = @Exchange(name = RabbitMqConstant.ADD_DETAIL_DERECT, type = ExchangeTypes.DIRECT),
            key = RabbitMqConstant.ADD_DETAIL_KEY
    ))
    private void addDetailInfo(String jsonStr) {
        DetailInfoVo detailInfo = JSONUtil.toBean(jsonStr, DetailInfoVo.class);
        boolean b = iDetailInfoRespository.addDetailInfo(detailInfo);
        //添加到区块链流水表中
        if (!b) {
            log.error("添加至mysql流水表失败 : "+jsonStr);
        }

    }
    //使用注解获取队列没有则会创建
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct5.queue55", declare = "true"),
            exchange = @Exchange(name = "nbsb.direct5", type = ExchangeTypes.DIRECT)
    ))
    public void test2(String msg) {
        System.out.println(msg);
    }

}
