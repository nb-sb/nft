package com.nft.domain.order.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Result;
import com.nft.common.Utils.OrderNumberUtil;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.domain.order.model.res.OrderRes;
import com.nft.domain.order.model.vo.UserOrderSimpleVo;
import com.nft.domain.support.mq.MqOperations;
import com.nft.domain.nft.model.req.AddUserConllection2MysqlReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.vo.*;
import com.nft.domain.nft.repository.IDetailInfoRespository;
import com.nft.domain.order.respository.INftOrderRespository;
import com.nft.domain.order.respository.IOrderInfoRespository;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.domain.order.service.INftOrderService;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Log4j2
@Service
@AllArgsConstructor
public class NftOrderService implements INftOrderService {

    private final INftOrderRespository iNftOrderRespository;
    private final ISubmitCacheRespository iSubmitCacheRespository;
    private final RedisUtil redisUtil;
    private final IUserInfoRepository iUserInfoRepository;
    private final IOrderInfoRespository iOrderInfoRespository;
    private final IOwnerShipRespository iOwnerShipRespository;
    private final IDetailInfoRespository iDetailInfoRespository;
    private final MqOperations mqOperations;
    private final ISellInfoRespository iSellInfoRespository;





    @Override
    @Transactional
    public Result addConllectionOrder(UserVo fromUser, Integer conllectionId) {
        //获取使用token用户id
        Integer userid = fromUser.getId();
        Integer stock ;

        //添加一把用户锁防止重复提交
        DistributedRedisLock.acquire(Constants.RedisKey.ADD_ORDER_BYUSER(userid));
        //判断是否有同一个商品未支付的，如果有则无法提交
        List<OrderInfoVo> userNoPayOrder = getUserNoPayOrder(fromUser.getId(), conllectionId);
        if (userNoPayOrder.size() > 0) {
            return new Result("0", "您的订单列表中此商品未支付！不能重复添加订单");
        }
        //已拥有的hash不能再次购买
        SellInfoVo sellInfoVo = iSellInfoRespository.selectSellInfoById(conllectionId);
        OwnerShipVo ownerShipVo = iOwnerShipRespository.selectOWnerShipInfo(fromUser.getAddress(), sellInfoVo.getIpfsHash());
        if (ownerShipVo != null) {
            return new Result("0", "同一藏品仅能存在一个，让给其他小伙伴吧！");
        }
        try {
            //1.添加写锁 -- 因为这个会改变出售的商品信息，所以要和查询商品id用同一把锁--这里添加写锁后就可以和查询商品信息的读锁连用
            DistributedRedisLock.acquireWriteLock(Constants.RedisKey.READ_WRITE_LOCK(conllectionId));
            try {
                //2.查询剩余库存
                ConllectionInfoVo conllectionInfoVo ;
                //查询mysql
                conllectionInfoVo = iSellInfoRespository.selectConllectionById(conllectionId);
                if (conllectionInfoVo == null) {
                    return new Result("0", "商品不存在");
                }
                stock = conllectionInfoVo.getRemain();
                if (stock <= 0) return new Result("0", "商品库存不足");
                //3.减少库存操作
                if (!iSellInfoRespository.setSellStocks(conllectionId, stock-1)) {
                    log.error("商品库存减少失败");
                    return new Result("0", "库存减少失败");
                }
                // 4.将藏品添加至订单中
                String orderNo = OrderNumberUtil.generateOrderNumber(userid, conllectionInfoVo.getId());
                boolean b = iOrderInfoRespository.addOrderInfo(conllectionInfoVo, userid,orderNo);
                if (!b) {
                    throw new APIException(Constants.ResponseCode.NO_UPDATE, "藏品添加至订单失败");
                }
                //1)decrRemain
                //2）更新redis中的商品信息 // 这里实际上可以直接将缓存给删掉，等下次查询的时候自动更新最新的，无需修改
                redisUtil.del(Constants.RedisKey.REDIS_COLLECTION(conllectionId));
                // 5.发送mq请求，半小时后检查订单状态
                mqOperations.SendCheckMessage(orderNo);
            } finally {
                //释放写锁
                DistributedRedisLock.releaseWriteLock(Constants.RedisKey.READ_WRITE_LOCK(conllectionId));
            }
        } finally {
            DistributedRedisLock.release(Constants.RedisKey.ADD_ORDER_BYUSER(userid));
        }
        return new Result("1", "成功添加至订单，请前往我的订单中查看");
    }

    @Override
    @Transactional
    public Result payOrder(UserVo fromUser,String orderNumber,Integer paytype) {
        //传入用户id和订单号和支付方式
        Integer userid = fromUser.getId();
        // 加锁(例如代付情况或点多次支付请求) 判断订单是否已经修改，除了 订单状态为 0 刚创建或者是 状态为1 未支付，否则无法支付订单
        DistributedRedisLock.acquire(Constants.RedisKey.PAY_LOCK(orderNumber));
        try {
            //使用订单号查询订单信息
            OrderInfoVo orderInfoVo = iOrderInfoRespository.selectOrderInfoByNumber(orderNumber);
            // 返回 订单号不存在
            if (orderInfoVo == null) return Result.error("订单号不存在");
            if (!Constants.payOrderStatus.NO_PAY.equals(orderInfoVo.getStatus())) {
                return Result.error("判断订单支付状态已经被修改，无法支付");
            }
            // 一.查询订单支付方式
            //A.如果是网站余额支付
            if (Constants.payType.WEB_BALANCE_PAY.equals(paytype)) {
                //1)decrement balance
                BigDecimal productPrice = orderInfoVo.getProductPrice();//查询商品价格
                //减少用于余额
                if (!iUserInfoRepository.decrementUserBalance(userid, productPrice)) {
                    //return 余额不足
                    return Result.error("余额不足");
                }
                //如果余额减少成功了的话则会：
                //2)set pay_status 设置支付状态
                boolean b = iOrderInfoRespository.setPayOrderStatus(orderNumber, Constants.payOrderStatus.PAID);
                if (!b) {
                    //设置支付状态失败。返回用于余额
                    log.error("支付状态修改失败");
                    throw new APIException(Constants.ResponseCode.NO_UPDATE, "支付状态修改失败");
                }
                //3)todo 返回购买状态（应该是直接在扣除余额成功的时候就可以返回了，然后后面操作发送到mq异步操作）
                //如果支付成功则转移藏品 => transferConllection()
                DetailInfoVo detailInfoVo1 = transferConllection(orderInfoVo, fromUser);
                // 加入mysql流水表中 || 加入区块链流水表中
                boolean b1 = addDetailInfo(detailInfoVo1);
                if (b1) {
                    //流水表添加成功
                    return Result.success("购买成功！");
                } else {
                    log.error("添加到流水表失败");
                }
            }
            //B.如果是支付宝/微信支付
            //1.请求支付信息，2.展示支付二维码，3.返回回调信息
            //1)decrement balance
            //2)set pay_status
            //3)返回支付支付状态
            //如果支付成功则转移藏品 => transferConllection()
            return Result.error("支付类型错误");
        }finally {
            DistributedRedisLock.releaseReadLock(Constants.RedisKey.PAY_LOCK(orderNumber));
        }

    }


    @Override
    public Result updataConllectionInfo(UpdataCollectionReq updataCollectionReq) {
        //1.添加写锁
        DistributedRedisLock.acquireWriteLock(Constants.RedisKey.READ_WRITE_LOCK(updataCollectionReq.getId()));
        try {
            //2.更新藏品信息
            //1)updataConllectionInfo
            if (updataCollectionReq.getName() != null || updataCollectionReq.getPresent() != null) {
                boolean b = iSellInfoRespository.updataConllectionInfo(updataCollectionReq);//更新藏品信息
                return b ? Result.success("更新藏品信息成功!") : Result.error("更新藏品信息失败!");
            }
            if (updataCollectionReq.getStatus() != null) {
                boolean b = iSubmitCacheRespository.updataSellStatus(updataCollectionReq);// 如果有状态的话则会更新出售表中状态
                return b ? Result.success("更新出售状态成功!") : Result.error("更新出售状态失败!");
            }
            return Result.error("什么都没执行");
        } finally {
            // 3.更新缓存数据
            //1）更新redis和es中数据 // 这里实际上可以直接将缓存给删掉，等下次查询的时候自动更新最新的，无需修改
            String reidsKey = Constants.RedisKey.REDIS_COLLECTION(updataCollectionReq.getId());
            redisUtil.del(reidsKey);
            //释放写锁
            DistributedRedisLock.releaseWriteLock(Constants.RedisKey.READ_WRITE_LOCK(updataCollectionReq.getId()));
        }
    }

    @Override
    public Result selectAllOrder(Page page) {
        List<OrderInfoVo> orderInfoVos = iNftOrderRespository.selectAllOrder(page);
        if (orderInfoVos != null) {
            return OrderRes.success(orderInfoVos);
        }
        return OrderRes.success("success");
    }

    @Override
    public Result getOrder(Integer userId) {
        List<UserOrderSimpleVo> orderInfoVos =  iNftOrderRespository.getOrder(userId);
        return OrderRes.success(orderInfoVos);
    }

    @Override
    public Result getOrder(Integer userId, String orderId) {
        List<OrderInfoVo> orderInfoVos =  iNftOrderRespository.getOrder(userId,orderId);
        return OrderRes.success(orderInfoVos);
    }

    @Override
    public Result getOrderByStatus() {
        return null;
    }


    //更新藏品所有者
    public DetailInfoVo transferConllection(OrderInfoVo orderInfo,UserVo user) {
        //购买成功后更新藏品所有者
        ConllectionInfoVo conllectionInfoVo = iSellInfoRespository.selectConllectionById(orderInfo.getProductId());
        //1）更新区块链上数据 => transCollectionByFisco(用户地址，藏品hash)
        boolean b = iOwnerShipRespository.addUserConllectionByFisco(user.getAddress(), conllectionInfoVo.getIpfsHash());
        if (!b) {
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "用户绑定藏品失败");
        }
        //如果更新区块链上数据成功的话，进行更新数据库内容
        //2）更新mysql上数据//更新数据库中用户拥有的藏品
        //查询fisco中存储的数据赋值到mysql中
        SellInfoVo sellInfoVo = iSellInfoRespository.selectSellInfoById(orderInfo.getProductId());
        //a.从fisco中获取所属信息 [1,"1703683065626","1","1#80"]
        List list = iOwnerShipRespository.selectOWnerShipInfoByFisco(user.getAddress(), sellInfoVo.getIpfsHash());
        System.out.println(list);
        if (!"1".equals(String.valueOf(list.get(0)))){
            //如果状态不等于一说明没有查到fisco中的数据
            log.error("fisco中属于查询失败："+list);
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "fisco中属于查询失败");
        }
        //b.添加至mysql中
        AddUserConllection2MysqlReq req = new AddUserConllection2MysqlReq();
        Date time = AddUserConllection2MysqlReq.timestamp2Date(String.valueOf(list.get(1)));
        req.setTime(time);
        Integer type = Integer.valueOf(String.valueOf(list.get(2)));
        req.setType(type);
        String digitalCollectionId = String.valueOf(list.get(3));
        req.setDigital_collection_id(digitalCollectionId);
        req.setHash(sellInfoVo.getIpfsHash());
        boolean b1 = iOwnerShipRespository.addUserConllection(req, user.getAddress());
        if (b1) {
            //c.更新订单状态为完成
            boolean b2 = iOrderInfoRespository.setOrderStatus(orderInfo.getOrderNo(), Constants.payOrderStatus.FINISH);
            if (!b2) {
                log.error("更新订单状态为完成 失败");
                throw new APIException(Constants.ResponseCode.NO_UPDATE, "更新订单状态为完成执行失败");
            }
            DetailInfoVo detailInfoVo = new DetailInfoVo();
            detailInfoVo.setType(type);
            detailInfoVo.setTransferAddress(sellInfoVo.getAuther());
            detailInfoVo.setTargetAddress(user.getAddress());
            detailInfoVo.setTime(time);
            detailInfoVo.setHash(sellInfoVo.getIpfsHash());
            detailInfoVo.setDigitalCollectionId(digitalCollectionId);
            return detailInfoVo;
        } else {
            //添加藏品到用户中失败
            log.error("添加藏品到用户中失败");
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "添加藏品到用户中失败");
        }
    }

    //添加流水表信息
    private boolean addDetailInfo(DetailInfoVo detailInfoVo1) {
        boolean b = iDetailInfoRespository.addDetailInfo(detailInfoVo1);
        //添加到区块链流水表中
        if (b) return iDetailInfoRespository.addDeailInfoByFisco(detailInfoVo1);
        return false;
    }

    //查询用户未支付订单
    private List<OrderInfoVo> getUserNoPayOrder(Integer userId,Integer collection) {
        return iOrderInfoRespository.selectOrderInfoByUser(userId, collection, Constants.payOrderStatus.NO_PAY);
    }

    //查询用户订单状态
    private Integer getUserOrderStatus(Integer userId, String orderNumber) {
        return iOrderInfoRespository.selectOrderStatusByUser(userId, orderNumber);
    }


}
