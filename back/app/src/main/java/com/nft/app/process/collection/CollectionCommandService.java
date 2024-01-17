package com.nft.app.process.collection;

import com.nft.app.process.collection.dto.ReviewCmd;
import com.nft.app.process.collection.dto.TransferCmd;
import com.nft.app.mq.producer.OrderPublisher;
import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Result;
import com.nft.common.Utils.TimeUtils;
import com.nft.domain.apply.model.entity.SubmitSellEntity;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.apply.service.INftSubmitService;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.vo.DetailInfoVo;
import com.nft.domain.nft.model.vo.OwnerShipVo;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.domain.support.ipfs.IpfsService;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.metal.MetalBorders;
import java.math.BigInteger;

@Log4j2
@Service
@AllArgsConstructor
public class CollectionCommandService {
    private final ISubmitCacheRespository iSubmitCacheRespository;
    private final RedisUtil redisUtil;
    private final OrderPublisher orderPublisher;
    private final IOwnerShipRespository iOwnerShipRespository;
    private final ISellInfoRespository iSellInfoRespository;
    private final DistributedRedisLock distributedRedisLock;
    private final INftSubmitService iNftSubmitService;
    private final IpfsService ipfsService;

    @Transactional
    public Result transferCollection(TransferCmd cmd) {
        String privatekey = cmd.getUserPrivatekey();
        String fromAddress = cmd.getUserAddress();
        String toAddress = cmd.getToAddress();
        Integer id = cmd.getId(); //所属藏品id
        if (StringUtil.equals(fromAddress, toAddress)) {
            return Result.error("不能将藏品转移给自己");
        }
        // TODO: 2024/1/4 判断toAddress是否存在
        //验证是否是自己的藏品
        OwnerShipVo myConllection = iOwnerShipRespository.getMyConllection(id, fromAddress);
        if (myConllection == null) {
            //返回 藏品不是你的
            return Result.error("该藏品不是你的，或藏品不存在");
        }
        boolean b1 = iOwnerShipRespository.transferCollectionByFisco(privatekey,myConllection.getHash(), toAddress, id);
        if (!b1) {
            log.error("区块链中：藏品转移错误");
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "区块链中：藏品转移错误");
        }
        //更新数据库中所属数据
        boolean b = iOwnerShipRespository.transferCollection(fromAddress, toAddress, id);
        if (!b) {
            log.error("更新流水表进行记录错误: "+fromAddress+","+ toAddress+","+id);

        }

        //调用区块链中转移方法 -- 合约中自动添加流水表 //使用所有者进行调用
        //更新msql流水表进行记录
        DetailInfoVo detailInfoVo = new DetailInfoVo();
        detailInfoVo.setTime(TimeUtils.getCurrent())
                .setHash(myConllection.getHash())
                .setTransferAddress(myConllection.getAddress())
                .setTargetAddress(toAddress)
                .setDigitalCollectionId(myConllection.getDigitalCollectionId())
                .setType(Constants.CollectionOwnerShipType.transfer);
        orderPublisher.SendDetailInfo(detailInfoVo);
        return Result.success("藏品转移成功！");
    }
    //修改商品信息 -1.可以在用户购买商品的时候用于更新剩余信息等 2.管理员更新藏品信息的时候用
    public Result updataConllectionInfo(UpdataCollectionReq updataCollectionReq) {
        //1.添加写锁
        distributedRedisLock.acquireWriteLock(Constants.RedisKey.READ_WRITE_LOCK(updataCollectionReq.getId()));
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
            distributedRedisLock.releaseWriteLock(Constants.RedisKey.READ_WRITE_LOCK(updataCollectionReq.getId()));
        }
    }
    @Transactional
    public Result ReviewCollection(ReviewCmd cmd) {
        // 需要先修改区块链状@态在修改mysql状态！！
        //这里因为是在审核中，所以不需要使用读写锁，普通的redisson锁即可防止多个管理员同时审核，造成数据库、区块链或ipfs多次上传等情况
        distributedRedisLock.release(Constants.RedisKey.ADMIN_UPDATE_LOCK(cmd.getId()));
        try {
            //2.操作审核状态 （1 为不通过 2 为通过）
            //获取提交数据
            SubmitSellEntity submitSellEntity = iSubmitCacheRespository.selectById(cmd.getId());
            if (submitSellEntity == null) {
                return AuditRes.error("需要审核的id不存在");
            }
            //必须是没有被审核过的才可以进行审核操作
            if (!Constants.SellState.DOING.getCode().equals(submitSellEntity.getStatus())) {
                return new AuditRes(Constants.SellState.NOTDOING.getCode(), Constants.SellState.NOTDOING.getInfo());
            }
            //判断修改前后变量结果是否一致，如果修改前后都是同一状态则不修改
            if (submitSellEntity.getStatus().equals(cmd.getStatus())) {
                return Result.error("修改前后状态一致");
            }
            if (Constants.SellState.REFUSE.getCode().equals(cmd.getStatus())) {
                //如果是不通过则这里直接返回参数，无需进行下面操作
                submitSellEntity.setStatus(cmd.getStatus());
                iSubmitCacheRespository.upDateSubStatus(submitSellEntity);
                return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
            }
            // -- 通过：通过才会加入到出售表，ipfs,区块链数据--
            //3.添加至ipfs 获得hash
            String ipfshash = ipfsService.addIpfsById(submitSellEntity.getPath());
            log.info("添加至ipfs 获得hash : " + ipfshash);
            //4.添加至区块链
            boolean addFISCO = iSellInfoRespository.addSellByFISCO(ipfshash, BigInteger.valueOf(submitSellEntity.getTotal()));
            if (!addFISCO) {
                return new AuditRes(Constants.SellState.ERRORFISCO.getCode(), Constants.SellState.ERRORFISCO.getInfo());
            }
            //5.保存审核内容至mysql 出售表中 - 1.修改提交表结果 2.修改出售表结果 上架出售
            submitSellEntity.setStatus(cmd.getStatus());
            iSubmitCacheRespository.upDateSubStatus(submitSellEntity); //修改提交表状态

            if (!iNftSubmitService.insertSellInfo(submitSellEntity, ipfshash)) {
                return new AuditRes(Constants.SellState.ERROR.getCode(), Constants.SellState.ERROR.getInfo());
            }
            // TODO: 2024/1/10 使用异步添加24h redis缓存
            return new AuditRes(Constants.SellState.PASS.getCode(), Constants.SellState.PASS.getInfo());
        } finally {
            distributedRedisLock.acquire(Constants.RedisKey.ADMIN_UPDATE_LOCK(cmd.getId()));
        }
    }
}
