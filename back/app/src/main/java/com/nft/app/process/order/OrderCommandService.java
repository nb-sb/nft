package com.nft.app.process.order;

import cn.hutool.json.JSONUtil;
import com.nft.app.mq.producer.OrderPublisher;
import com.nft.app.process.order.dto.PayOrderCmd;
import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisConstant;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Result;
import com.nft.common.Utils.TimeUtils;
import com.nft.domain.nft.model.entity.OwnerShipEntity;
import com.nft.domain.nft.model.entity.SellInfoEntity;
import com.nft.domain.nft.model.req.AddOrderCmd;
import com.nft.domain.nft.model.vo.*;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.domain.nft.service.INftInfoService;
import com.nft.domain.nft.service.OwnerShipFactory.OwnerShipEntityFatory;
import com.nft.domain.order.model.entity.OrderEntity;
import com.nft.domain.order.model.req.AddOrderMqMessage;
import com.nft.domain.order.respository.IOrderInfoRespository;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.Factory.UserEntityFatory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class OrderCommandService {
    private final RedisUtil redisUtil;
    private final IUserInfoRepository iUserInfoRepository;
    private final IOwnerShipRespository iOwnerShipRespository;
    private final IOrderInfoRespository iOrderInfoRespository;
    private final OrderPublisher orderPublisher;
    private final ISellInfoRespository iSellInfoRespository;
    private final DistributedRedisLock distributedRedisLock;
    private final UserEntityFatory userEntityFatory;
    private final OwnerShipEntityFatory ownerShipEntityFatory;
    private final INftInfoService iNftInfoService;

    // 增加用户的应用服务方法
    public void addUser(String username, String address, String password, String privatekey, BigDecimal balance, Integer role) {

//        UserAggregate userAggregate = new UserAggregate();
//        userAggregate.addUser(username, address, password, privatekey, balance, role);
        UserEntity userEntity = userEntityFatory.newInstance("username", "address", "pw", "pv", null, 0);

        // 保存用户实体到仓储
        iUserInfoRepository.creat(userEntity);
    }
    public Result creat(AddOrderCmd cmd) {
        //获取使用token用户id
        Integer userId = cmd.getUserId();
        String userAddress = cmd.getUserAddress();
        Integer collectionId = cmd.getCollectionId();
        Date time = TimeUtils.getCurrent();
        //添加一把用户锁防止重复提交
        distributedRedisLock.acquire(Constants.RedisKey.ADD_ORDER_BYUSER(userId));
        try {
            //1.添加写锁 -- 因为这个会改变出售的商品信息，所以要和查询商品id用同一把锁--这里添加写锁后就可以和查询商品信息的读锁连用
            distributedRedisLock.acquireWriteLock(Constants.RedisKey.READ_WRITE_LOCK(collectionId));
            try {
                Integer stock;
                //2.查询剩余库存
                String reidsKey = Constants.RedisKey.REDIS_COLLECTION(collectionId);
                ConllectionInfoVo conllectionInfoVo = iSellInfoRespository.selectCacheByCollectId(collectionId);

                if (conllectionInfoVo != null) {
                    stock = conllectionInfoVo.getRemain();
                    if (stock <= 0) return new Result("0", "商品库存不足");
                } else {
                    //查询mysql
                    conllectionInfoVo = iNftInfoService.selectByCollectId(collectionId);
                    if (conllectionInfoVo == null) {
                        redisUtil.set(reidsKey, Constants.RedisKey.REDIS_EMPTY_CACHE(), RedisConstant.MINUTE_30);
                        return new Result("0", "商品不存在");
                    }
                    stock = conllectionInfoVo.getRemain();
                    redisUtil.set(reidsKey, JSONUtil.toJsonStr(conllectionInfoVo), RedisConstant.MINUTE_30);
                    if (stock <= 0) return new Result("0", "商品库存不足");
                }
                //判断是否有同一个商品未支付的，如果有则无法提交
                List<OrderInfoVo> userNoPayOrder = iOrderInfoRespository.selectOrderInfoByUser(userId, collectionId, Constants.payOrderStatus.NO_PAY);
                if (userNoPayOrder.size() > 0) {
                    return new Result("0", "您的订单列表中此商品未支付！不能重复添加订单");
                }
                //已拥有的hash不能再次购买
                SellInfoEntity sellInfoEntity =  iSellInfoRespository.selectSellInfoById(collectionId);
                OwnerShipVo ownerShipVo = iOwnerShipRespository.selectOWnerShipInfo(userAddress, sellInfoEntity.getIpfsHash());
                if (ownerShipVo != null) {
                    return new Result("0", "同一藏品仅能存在一个，让给其他小伙伴吧！");
                }
                //3.减少库存操作
                if (!iSellInfoRespository.setSellStocks(collectionId, stock - 1)) {
                    log.error("商品库存减少失败");
                    return new Result("0", "库存减少失败");
                } else {
                    //更新商品缓存操作
                    conllectionInfoVo.setRemain(stock - 1);
                    redisUtil.set(reidsKey, JSONUtil.toJsonStr(conllectionInfoVo), RedisConstant.MINUTE_30);
                }
            } finally {
                //释放写锁
                distributedRedisLock.releaseWriteLock(Constants.RedisKey.READ_WRITE_LOCK(collectionId));
            }
        } finally {
            distributedRedisLock.release(Constants.RedisKey.ADD_ORDER_BYUSER(userId));
        }
        // 4.将藏品添加至订单中
        //   发送mq操作数据库写入订单
        AddOrderMqMessage addOrderMqMessage = new AddOrderMqMessage()
                .setConllectionId(collectionId)
                .setTime(time)
                .setUserId(userId);
        orderPublisher.SendAddOrderMessage(addOrderMqMessage);
        return new Result("1", "成功添加至订单，请前往我的订单中查看");
    }
    //支付订单
    @Transactional
    public Result payOrder(PayOrderCmd payOrderCmd) {
        //传入用户id和订单号和支付方式
        Integer userid = payOrderCmd.getUserId();
        String orderNumber = payOrderCmd.getOrderNumber();
        Integer paytype = payOrderCmd.getPaytype();
        // 加锁(例如代付情况或点多次支付请求) 判断订单是否已经修改，除了 订单状态为 0 刚创建或者是 状态为1 未支付，否则无法支付订单
        distributedRedisLock.acquire(Constants.RedisKey.PAY_LOCK(orderNumber));
        try {
            //使用订单号查询订单信息
            OrderEntity orderEntity = iOrderInfoRespository.selectOrderInfoByNumber(orderNumber);
            // 返回 订单号不存在
            if (orderEntity == null) return Result.error("订单号不存在");
            if (!Constants.payOrderStatus.NO_PAY.equals(orderEntity.getStatus())) {
                return Result.error("订单支付状态已经被修改，无法支付");
            }
            // 一.查询订单支付方式
            //A.如果是网站余额支付
            if (Constants.payType.WEB_BALANCE_PAY.equals(paytype)) {
                //1)decrement balance
                BigDecimal productPrice = orderEntity.getProductPrice();//查询商品价格
                //查询用于余额，减少余额，设置余额
                UserEntity userEntity = iUserInfoRepository.selectOneById(userid);
                if (userEntity == null) {
                    return Result.error("支付用户不存在！");
                }
                userEntity.decreaseBalance(productPrice);
                //减少用于余额
                boolean b2 = iUserInfoRepository.saveBalance(userEntity);
                if (!b2) {
                    //return 余额不足
                    return Result.error("设置余额失败");
                }
                //如果余额减少成功了的话则会：
                //2)set pay_status 设置支付状态
                orderEntity.setStatus(Constants.payOrderStatus.PAID);
                boolean b = iOrderInfoRespository.save(orderEntity);
                if (!b) {
                    //设置支付状态失败。返回用于余额
                    log.error("支付状态修改失败");
                    throw new APIException(Constants.ResponseCode.NO_UPDATE, "支付状态修改失败");
                }
                //todo 发送mq消息异步
                //如果支付成功则添加用户藏品
                DetailInfoVo detailInfoVo1 = addUserCollection(orderEntity, payOrderCmd.getUserAddress());
                // 加入mysql流水表中 || 加入区块链流水表中
                orderPublisher.SendDetailInfo(detailInfoVo1);
                return Result.success("购买成功！");
            }
            //B.如果是支付宝/微信支付
            //1.请求支付信息，2.展示支付二维码，3.返回回调信息
            //1)decrement balance
            //2)set pay_status
            //3)返回支付支付状态
            //如果支付成功则转移藏品 => transferConllection()
            return Result.error("支付类型错误");
        }finally {
            distributedRedisLock.releaseReadLock(Constants.RedisKey.PAY_LOCK(orderNumber));
        }
    }

    //添加新藏品所有者
    @Transactional
    public DetailInfoVo addUserCollection( OrderEntity orderEntity, String userAddress) {
        Integer productId = orderEntity.getProductId();
        //购买成功后更新藏品所有者
        SellInfoEntity sellInfoEntity = iSellInfoRespository.selectSellInfoById(productId);
        //1）更新区块链上数据 => transCollectionByFisco(用户地址，藏品hash)
        boolean b = iOwnerShipRespository.addUserConllectionByFisco(userAddress, sellInfoEntity.getIpfsHash());
        if (!b) {
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "用户绑定藏品失败");
        }
        //如果更新区块链上数据成功的话，进行更新数据库内容
        //2）更新mysql上数据//更新数据库中用户拥有的藏品
        //查询fisco中存储的数据赋值到mysql中
        //a.从fisco中获取所属信息 [1,"1703683065626","1","1#80"]
        List list = iOwnerShipRespository.selectOWnerShipInfoByFisco(userAddress, sellInfoEntity.getIpfsHash());
        System.out.println(list);
        if (!"1".equals(String.valueOf(list.get(0)))){
            //如果状态不等于一说明没有查到fisco中的数据
            log.error("fisco中属于查询失败："+list);
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "fisco中属于查询失败");
        }
        //b.添加至mysql中
        Integer type = Integer.valueOf(String.valueOf(list.get(2)));
        String digitalCollectionId = String.valueOf(list.get(3));
        OwnerShipEntity ownerShipEntity = ownerShipEntityFatory.newInstance(userAddress, type, digitalCollectionId, sellInfoEntity.getIpfsHash());
        ownerShipEntity.timestamp2Date(String.valueOf(list.get(1)));
        boolean b1 = iOwnerShipRespository.creat(ownerShipEntity);
        if (b1) {
            //c.更新订单状态为完成
            orderEntity.setStatus(Constants.payOrderStatus.FINISH);
            boolean b2 = iOrderInfoRespository.save(orderEntity);
            if (!b2) {
                log.error("更新订单状态为完成 失败");
                throw new APIException(Constants.ResponseCode.NO_UPDATE, "更新订单状态为完成执行失败");
            }
            DetailInfoVo detailInfoVo = new DetailInfoVo();
            detailInfoVo.setType(type);
            detailInfoVo.setTransferAddress(sellInfoEntity.getAuther());
            detailInfoVo.setTargetAddress(userAddress);
            detailInfoVo.setTime(ownerShipEntity.getTime());
            detailInfoVo.setHash(ownerShipEntity.getHash());
            detailInfoVo.setDigitalCollectionId(digitalCollectionId);
            return detailInfoVo;
        } else {
            //添加藏品到用户中失败
            log.error("添加藏品到用户中失败");
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "添加藏品到用户中失败");
        }
    }

}
