package com.nft.domain.nft.service.impl;


import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Result;
import com.nft.common.Utils.TokenUtils;
import com.nft.domain.nft.model.req.AddUserConllection2MysqlReq;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.model.vo.*;
import com.nft.domain.nft.repository.IDetailInfoRespository;
import com.nft.domain.nft.repository.INftSellRespository;
import com.nft.domain.nft.repository.IOrderInfoRespository;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.domain.nft.service.INftSellService;
import com.nft.domain.support.ipfs.IpfsService;
import com.nft.domain.user.model.req.LoginReq;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.tree.VoidDescriptor;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Log4j2
@Service
@AllArgsConstructor
public class NftSellService implements INftSellService {

    private final INftSellRespository iNftSellRespository;
    private final RedisUtil redisUtil;
    private final IpfsService ipfsService;
    private final IUserInfoRepository iUserInfoRepository;
    private final IOrderInfoRespository iOrderInfoRespository;
    private final IOwnerShipRespository iOwnerShipRespository;
    private final IDetailInfoRespository iDetailInfoRespository;

    @Override
    public NftRes addSellCheck(HttpServletRequest httpServletRequest, SellReq sellReq) {
        UserVo userVo = decodeToken(httpServletRequest);
        if (userVo == null) return new NftRes("401", "用户token错误请从新登录");
        boolean res = iNftSellRespository.addSellCheck(sellReq, userVo);
        if (res) return new NftRes("1", "已经添加到审核中~");
        return new NftRes("0", "添加审核失败，请联系网站管理员查看");
    }

    @Override
    public AuditRes changeSellStatus(ReviewReq req) {
        //判断修改前后变量结果是否一致，如果修改前后都是同一状态则不修改
        SubCacheVo subCacheVo = iNftSellRespository.selectSubSellById(req.getId());
        if (!Constants.SellState.DOING.getCode().equals(subCacheVo.getStatus())) {
            return new AuditRes(Constants.SellState.NOTDOING.getCode(), Constants.SellState.NOTDOING.getInfo());
        }
        if (subCacheVo.getStatus().equals(req.getStatus())) {
            log.warn("修改前后状态一致！");
            return new AuditRes(Constants.SellState.ERROR.getCode(), Constants.SellState.ERROR.getInfo());
        }
        if (Constants.SellState.REFUSE.getCode().equals(req.getStatus())) {
            //如果是不同意的话这里可以直接修改审核结果然后返回到controller中
            iNftSellRespository.upDateSubStatus(req);
            return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
        }
        if (Constants.SellState.PASS.getCode().equals(req.getStatus())) {
            return new AuditRes(Constants.SellState.PASS.getCode(), Constants.SellState.PASS.getInfo());
        }
        return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
    }


    @Override
    public boolean insertSellInfo(ReviewReq req, String hash) {
        boolean b = iNftSellRespository.upDateSubStatus(req); //修改提交表
        boolean b1 = iNftSellRespository.insertSellInfo(req.getId(), hash); //增加出售表
        return b && b1;
    }



    @Override
    public Result purchaseConllection(HttpServletRequest httpServletRequest, Integer conllectionId) {
        //获取使用token用户id
        UserVo user = decodeToken(httpServletRequest);
        if (user == null) return new Result("401", "用户登录错误");
        Integer userid = user.getId();
        Integer stock ;

        //添加一把用户锁防止重复提交
        DistributedRedisLock.acquire(Constants.RedisKey.ADD_ORDER_BYUSER(userid));
        //判断是否有同一个商品未支付的，如果有则无法提交
        List<OrderInfoVo> userNoPayOrder = getUserNoPayOrder(user.getId(), conllectionId);
        if (userNoPayOrder.size() > 0) {
            return new Result("0", "您的订单列表中此商品未支付！不能重复添加订单");
        }
        try {
            //1.添加写锁 -- 因为这个会改变出售的商品信息，所以要和查询商品id用同一把锁--这里添加写锁后就可以和查询商品信息的读锁连用
            DistributedRedisLock.acquireWriteLock(Constants.RedisKey.READ_WRITE_LOCK(conllectionId));
            try {
                //2.查询剩余库存
                ConllectionInfoVo conllectionInfoVo ;
                //查询mysql缓存
                conllectionInfoVo = iNftSellRespository.selectConllectionById(conllectionId);
                if (conllectionInfoVo == null) {
                    return new Result("0", "商品不存在");
                }
                stock = conllectionInfoVo.getRemain();
                if (stock <= 0) return new Result("0", "商品库存不足");
                //3.减少库存操作
                if (!iNftSellRespository.decreaseSellStocks(conllectionId, 1)) {
                    log.error("商品库存减少失败");
                    return new Result("0", "库存减少失败");
                }
                // 4.将藏品添加至订单中
                iOrderInfoRespository.addOrderInfo(conllectionInfoVo, userid);
                //1)decrRemain
                //2）更新redis中的商品信息 // 这里实际上可以直接将缓存给删掉，等下次查询的时候自动更新最新的，无需修改
                redisUtil.del(Constants.RedisKey.REDIS_COLLECTION(conllectionId));
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
    public Result payOrder(HttpServletRequest httpServletRequest,String orderNumber,Integer paytype) {
        //传入用户id和订单号和支付方式
        UserVo userVo = decodeToken(httpServletRequest);
        if (userVo == null) return new NftRes("401", "用户token错误请从新登录");
        Integer userid = userVo.getId();
        paytype = Constants.payType.WEB_BALANCE_PAY;
        // 加锁(例如代付情况或点多次支付请求) 判断订单是否已经修改，除了 订单状态为 0 刚创建或者是 状态为1 未支付，否则无法支付订单
        DistributedRedisLock.acquire(Constants.RedisKey.PAY_LOCK(orderNumber));
        try {
            //使用订单号查询订单信息
            OrderInfoVo orderInfoVo = iOrderInfoRespository.selectOrderInfoByNumber(orderNumber);
            // 返回 订单号不存在
            if (orderInfoVo == null) return new Result("0", "订单号不存在");
            if (!Constants.payOrderStatus.NO_PAY.equals(orderInfoVo.getStatus())) {
                return new Result("0", "判断订单支付状态已经被修改，无法支付");
            }
            // 一.查询订单支付方式
            //A.如果是网站余额支付
            if (Constants.payType.WEB_BALANCE_PAY.equals(paytype)) {
                //1)decrement balance
                BigDecimal productPrice = orderInfoVo.getProductPrice();//查询商品价格
                //减少用于余额
                if (iUserInfoRepository.decrementUserBalance(userid, productPrice)) {
                    //如果余额减少成功了的话则会：
                    //2)set pay_status 设置支付状态
                    boolean b = iOrderInfoRespository.setPayOrderStatus(orderNumber, Constants.payOrderStatus.PAID);
                    if (b) {
                        //3)todo 返回购买状态（应该是直接在扣除余额成功的时候就可以返回了，然后后面操作发送到mq异步操作）
                        //如果支付成功则转移藏品 => transferConllection()
                        DetailInfoVo detailInfoVo1 = transferConllection(orderInfoVo, userVo);
                        // 加入mysql流水表中 || 加入区块链流水表中
                        boolean b1 = addDetailInfo(detailInfoVo1);
                        if (b1) {
                            //流水表添加成功
                            return new Result("1", "购买成功！");
                        } else {
                            log.error("添加到流水表失败");
                        }
                    } else {
                        //设置支付状态失败。返回用于余额
                        log.error("支付状态修改失败");
                        throw new APIException(Constants.ResponseCode.NO_UPDATE, "支付状态修改失败");
                    }

                }else {
                    //return 余额不足
                    return new Result("0", "余额不足");
                }
            }
            //B.如果是支付宝/微信支付
            //1.请求支付信息，2.展示支付二维码，3.返回回调信息
            //1)decrement balance
            //2)set pay_status
            //3)返回支付支付状态
            //如果支付成功则转移藏品 => transferConllection()
            return  new Result("0", "支付类型错误");
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
                boolean b = iNftSellRespository.updataConllectionInfo(updataCollectionReq);//更新藏品信息
                return b ? new Result("1", "更新藏品信息成功!") : new Result("0", "更新藏品信息失败!");
            }
            if (updataCollectionReq.getStatus() != null) {
                boolean b = iNftSellRespository.updataSellStatus(updataCollectionReq);// 如果有状态的话则会更新出售表中状态
                return b ? new Result("1", "更新出售状态成功!") : new Result("0", "更新出售状态失败!");
            }
            return new Result("0", "什么都没执行");
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
    public AuditRes ReviewCollection(ReviewReq req) {
        // 需要先修改区块链状态在修改mysql状态！！
        //这里因为是在审核中，所以不需要使用读写锁，普通的redisson锁即可防止多个管理员同时审核，造成数据库、区块链或ipfs多次上传等情况
        DistributedRedisLock.release(Constants.RedisKey.ADMIN_UPDATE_LOCK(req.getId()));
        try {
            //2.操作审核状态 （1 为不通过 2为通过）
            AuditRes auditRes = changeSellStatus(req);
            if (!String.valueOf(Constants.SellState.PASS.getCode()).equals(auditRes.getCode())) {
                //如果是不通过则这里直接返回参数，无需进行下面操作
                return auditRes;
            }
            // -- 通过：通过才会加入到出售表，ipfs,区块链数据--
            //3.添加至ipfs 获得hash
            String hash = ipfsService.addIpfsById(String.valueOf(req.getId()));
            System.err.println("hash : " + hash);
            //4.添加至区块链
            boolean addFISCO = iNftSellRespository.addSellByFISCO(hash, req.getId());
            System.out.println(addFISCO);
            if (!addFISCO) {
                return new AuditRes(Constants.SellState.ERRORFISCO.getCode(), Constants.SellState.ERRORFISCO.getInfo());
            }
            //5.保存审核内容至mysql 出售表中 - 1.修改提交表结果 2.修改出售表结果 上架出售
            if (!insertSellInfo(req, hash)) {
                return new AuditRes(Constants.SellState.ERROR.getCode(), Constants.SellState.ERROR.getInfo());
            }
            return new AuditRes(Constants.SellState.PASS.getCode(), Constants.SellState.PASS.getInfo());
        } finally {
            DistributedRedisLock.acquire(Constants.RedisKey.ADMIN_UPDATE_LOCK(req.getId()));
        }


    }

    //更新藏品所有者
    public DetailInfoVo transferConllection(OrderInfoVo orderInfo,UserVo user) {
        //购买成功后更新藏品所有者
        ConllectionInfoVo conllectionInfoVo = iNftSellRespository.selectConllectionById(orderInfo.getProductId());
        //1）更新区块链上数据 => transCollectionByFisco(用户地址，藏品hash)
        boolean b = iOwnerShipRespository.addUserConllectionByFisco(user.getAddress(), conllectionInfoVo.getIpfsHash());
        if (!b) {
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "用户绑定藏品失败");
        }
        //如果更新区块链上数据成功的话，进行更新数据库内容
        //2）更新mysql上数据//更新数据库中用户拥有的藏品
        //查询fisco中存储的数据赋值到mysql中
        SellInfoVo sellInfoVo = iNftSellRespository.selectSellInfoById(orderInfo.getProductId());
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



    public UserVo  decodeToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Map<String, String> userMap = TokenUtils.decodeToken(token);
        String username = userMap.get("username");
        String pass = userMap.get("password");
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(username).setPassword(pass);
        return iUserInfoRepository.selectOne(loginReq);
    }
}
