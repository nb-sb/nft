package com.nft.domain.apply.service.Impl;

import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.apply.service.Factory.SubmitSellEntityFatory;
import com.nft.domain.apply.service.INftSubmitService;
import com.nft.domain.nft.model.entity.SellInfoEntity;
import com.nft.domain.apply.model.entity.SubmitSellEntity;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.domain.nft.service.SellInfoFactory.SellInfoEntityFatory;
import com.nft.domain.nftSort.model.entity.MetaEntity;
import com.nft.domain.nftSort.model.entity.MetaRelationShipEntity;
import com.nft.domain.nftSort.repository.INftRelationshipRespository;
import com.nft.domain.nftSort.repository.ISortRepository;
import com.nft.domain.user.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class INftSubmitServiceImpl implements INftSubmitService {
    private final ISubmitCacheRespository iSubmitCacheRespository;
    private final ISellInfoRespository iSellInfoRespository;
    private final SellInfoEntityFatory sellInfoEntityFatory;
    private final INftRelationshipRespository iNftRelationshipRespository;
    private final ISortRepository iSortRepository;
    private final SubmitSellEntityFatory submitSellEntityFatory;



    @Override
    public Result creat(UserEntity fromUser, ApplyReq applyReq) {

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


    @Override
    public boolean insertSellInfo(SubmitSellEntity submitSellEntity, String ipfshash) {
        SellInfoEntity sellInfoEntity = sellInfoEntityFatory.newInstance(submitSellEntity.getId(), submitSellEntity.getHash(), submitSellEntity.getTotal(), submitSellEntity.getAuthorAddress(), ipfshash);
        sellInfoEntity.init();
        return iSellInfoRespository.creat(sellInfoEntity);
    }

}
