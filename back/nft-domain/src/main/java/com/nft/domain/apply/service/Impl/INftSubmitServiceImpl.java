package com.nft.domain.apply.service.Impl;

import com.nft.common.Constants;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Result;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.apply.service.Factory.SubmitSellEntityFatory;
import com.nft.domain.apply.service.INftSubmitService;
import com.nft.domain.nft.model.entity.SellInfoEntity;
import com.nft.domain.apply.model.entity.SubmitSellEntity;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.domain.nft.service.SellInfoFactory.SellInfoEntityFatory;
import com.nft.domain.nftSort.model.entity.MetaEntity;
import com.nft.domain.nftSort.model.entity.MetaRelationShipEntity;
import com.nft.domain.nftSort.repository.INftRelationshipRespository;
import com.nft.domain.nftSort.repository.ISortRepository;
import com.nft.domain.support.ipfs.IpfsService;
import com.nft.domain.user.model.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Log4j2
@Service
@AllArgsConstructor
public class INftSubmitServiceImpl implements INftSubmitService {
    private final ISubmitCacheRespository iSubmitCacheRespository;
    private final IpfsService ipfsService;
    private final ISellInfoRespository iSellInfoRespository;
    private final DistributedRedisLock distributedRedisLock;
    private final SellInfoEntityFatory sellInfoEntityFatory;
    private final INftRelationshipRespository iNftRelationshipRespository;
    private final ISortRepository iSortRepository;
    private final SubmitSellEntityFatory submitSellEntityFatory;



    @Override
    public Result creat(UserVo fromUser, ApplyReq applyReq) {

        SubmitSellEntity subCache = iSubmitCacheRespository.selectOneByHash(applyReq.getHash());
        if (subCache != null) {
            log.info("hash 已经存在 - 无法提交重复的作品！"+subCache);
            return new NftRes("0", "hash 已经存在 - 无法提交重复的作品！");
        }
        //查询分类id是否存在
        if (!iSortRepository.isExist(applyReq.getMid())) {
            log.info("分类不存在："+ applyReq.getMid());
            return new NftRes("0", "分类不存在："+ applyReq.getMid());
        }
//        applyReq, fromUser
        SubmitSellEntity submitSellEntity = submitSellEntityFatory.newInstance(applyReq.getPath(), applyReq.getTotal(),
                applyReq.getPresent(), applyReq.getName(), applyReq.getPrice(), applyReq.getHash(), String.valueOf(fromUser.getId()), fromUser.getAddress());
        submitSellEntity.init();
        boolean res = iSubmitCacheRespository.creat(submitSellEntity);
        //查询缓存提交的id
        Integer id = iSubmitCacheRespository.selectIdByHash(applyReq.getHash());
        //生成entity
        MetaRelationShipEntity metaRelationShipEntity = new MetaRelationShipEntity();
        metaRelationShipEntity.setCid(id);
        metaRelationShipEntity.setMid(applyReq.getMid());
        //添加到数据库中
        iNftRelationshipRespository.creat(metaRelationShipEntity);
        //查询分类表，如果添加成功了，然后修改记录数
        MetaEntity metaEntity = iSortRepository.selectSortByMid(applyReq.getMid());
        if (metaEntity == null) {
            return Result.error("添加到数据库失败");
        }
        //修改分类表中分类记录数
        metaEntity.increaseCount();
        iSortRepository.updateSort(metaEntity);

        if (res) return new NftRes("1", "已经添加到审核中~");
        return new NftRes("0", "添加审核失败，请联系网站管理员查看");
    }


    public AuditRes changeSellStatus(SubmitSellEntity submitSellEntity,Integer status) {
        //必须是没有被审核过的才可以
        if (!Constants.SellState.DOING.getCode().equals(submitSellEntity.getStatus())) {
            return new AuditRes(Constants.SellState.NOTDOING.getCode(), Constants.SellState.NOTDOING.getInfo());
        }
        //判断修改前后变量结果是否一致，如果修改前后都是同一状态则不修改
        if (submitSellEntity.getStatus().equals(status)) {
            log.warn("修改前后状态一致！");
            return new AuditRes(Constants.SellState.ERROR.getCode(), Constants.SellState.ERROR.getInfo());
        }
        if (Constants.SellState.REFUSE.getCode().equals(status)) {
            //如果是不同意的话这里可以直接修改审核结果然后返回到controller中
            submitSellEntity.setStatus(status);
            iSubmitCacheRespository.upDateSubStatus(submitSellEntity);
            return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
        }
        if (Constants.SellState.PASS.getCode().equals(status)) {
            return new AuditRes(Constants.SellState.PASS.getCode(), Constants.SellState.PASS.getInfo());
        }
        return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
    }


    public boolean insertSellInfo(SubmitSellEntity submitSellEntity,Integer status, String ipfshash) {
        submitSellEntity.setStatus(status);
        boolean b = iSubmitCacheRespository.upDateSubStatus(submitSellEntity); //修改提交表
        SellInfoEntity sellInfoEntity = sellInfoEntityFatory.newInstance(submitSellEntity.getId(), submitSellEntity.getHash(), submitSellEntity.getTotal(), submitSellEntity.getAuthorAddress(), ipfshash);
        sellInfoEntity.init();
        boolean b1 = iSellInfoRespository.creat(sellInfoEntity); //增加出售表
        return b && b1;
    }
    @Override
    @Transactional
    public Result ReviewCollection(ReviewReq req) {
        // 需要先修改区块链状@态在修改mysql状态！！
        //这里因为是在审核中，所以不需要使用读写锁，普通的redisson锁即可防止多个管理员同时审核，造成数据库、区块链或ipfs多次上传等情况
        distributedRedisLock.release(Constants.RedisKey.ADMIN_UPDATE_LOCK(req.getId()));
        try {
            //2.操作审核状态 （1 为不通过 2为通过）\
            //获取提交数据
            SubmitSellEntity subCacheVo = iSubmitCacheRespository.selectById(req.getId());
            if (subCacheVo == null) {
                return AuditRes.error("需要审核的id不存在");
            }
            Result auditRes = changeSellStatus(subCacheVo,req.getStatus());
            if (!String.valueOf(Constants.SellState.PASS.getCode()).equals(auditRes.getCode())) {
                //如果是不通过则这里直接返回参数，无需进行下面操作
                return auditRes;
            }
            // -- 通过：通过才会加入到出售表，ipfs,区块链数据--
            //3.添加至ipfs 获得hash
            String ipfshash = ipfsService.addIpfsById(subCacheVo.getPath());
            log.info("ipfshash : " + ipfshash);
            //4.添加至区块链
            boolean addFISCO = iSellInfoRespository.addSellByFISCO(ipfshash, BigInteger.valueOf(subCacheVo.getTotal()));
            log.info("addFISCO : "+addFISCO);
            if (!addFISCO) {
                return new AuditRes(Constants.SellState.ERRORFISCO.getCode(), Constants.SellState.ERRORFISCO.getInfo());
            }
            //5.保存审核内容至mysql 出售表中 - 1.修改提交表结果 2.修改出售表结果 上架出售
            if (!insertSellInfo(subCacheVo,req.getStatus(), ipfshash)) {
                // TODO: 2024/1/15 添加分类表中
                return new AuditRes(Constants.SellState.ERROR.getCode(), Constants.SellState.ERROR.getInfo());
            }
            // TODO: 2024/1/10 使用异步添加24h redis缓存
            return new AuditRes(Constants.SellState.PASS.getCode(), Constants.SellState.PASS.getInfo());
        } finally {
            distributedRedisLock.acquire(Constants.RedisKey.ADMIN_UPDATE_LOCK(req.getId()));
        }


    }

    @Override
    public Result selectAllApplyPage() {
        return null;
    }
}
