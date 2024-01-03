package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.apply.model.vo.SubCacheVo;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.infrastructure.dao.SellInfoMapper;
import com.nft.infrastructure.dao.SubmitCacheMapper;
import com.nft.infrastructure.fisco.service.SellStroageService;
import com.nft.infrastructure.po.SellInfo;
import com.nft.infrastructure.po.SubmitCache;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Log4j2
@AllArgsConstructor
public class ISubmitCacheRespositoryImpl implements ISubmitCacheRespository {
    private final SubmitCacheMapper submitCacheMapper;
    private final SellInfoMapper sellInfoMapper;
    private final INftRelationshipImpl nftRelationship;
    private final INftMetasImpl nftMetas;

    @Override
    public SubCacheVo selectOneByHash(String hash) {
        QueryWrapper<SubmitCache> submitWrapper = new QueryWrapper<>();
        submitWrapper.eq("hash",hash);
        SubmitCache submitCache1 = submitCacheMapper.selectOne(submitWrapper);
        SubCacheVo subCacheVo = BeanCopyUtils.convertTo(submitCache1, SubCacheVo::new);
        return subCacheVo;
    }

    @Override
    @Transactional
    public boolean addSellCheck(SellReq sellReq, UserVo userVo) {
        SubmitCache submitCache = BeanCopyUtils.convertTo(sellReq, SubmitCache::new);
        submitCache.setStatus(0)
                .setAuthorId(String.valueOf(userVo.getId()))
                .setAuthorAddress(userVo.getAddress());
        QueryWrapper<SubmitCache> submitWrapper = new QueryWrapper<>();
        submitWrapper.eq("hash", submitCache.getHash());
        int insert = submitCacheMapper.insert(submitCache);
        SubmitCache submitCache1 = submitCacheMapper.selectOne(submitWrapper);
        if (insert > 0) {
            //添加分类表
            boolean b = nftRelationship.addMetas(submitCache1.getId(), sellReq.getMid());
            if (!b) {
                log.error("添加至分类表错误!: "+ false);
                throw new APIException(Constants.ResponseCode.NO_UPDATE, "添加至分类表错误");
            }
            //修改分类表中分类记录数
            boolean incr = nftMetas.incr(sellReq.getMid(), 1);
            if (!incr) {
                log.error("修改分类表中分类记录数 错误 ！："+ false);
                throw new APIException(Constants.ResponseCode.NO_UPDATE, "修改分类表中分类记录数错误");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean upDateSubStatus(ReviewReq req) {
        UpdateWrapper<SubmitCache> submitWrapper = new UpdateWrapper<>();
        submitWrapper.eq("id", req.getId());
        SubmitCache submitCache = new SubmitCache();
        submitCache.setStatus(req.getStatus());
        int update = submitCacheMapper.update(submitCache,submitWrapper);
        return update > 0;
    }

    @Override
    public boolean updataSellStatus(UpdataCollectionReq updataCollectionReq) {
        SellInfo sellInfo = new SellInfo().setStatus(updataCollectionReq.getStatus());
        UpdateWrapper<SellInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("id", updataCollectionReq.getId());
        int update = sellInfoMapper.update(sellInfo, updateWrapper);
        return update > 0;
    }

    @Override
    public SubCacheVo selectSubSellById(Integer id) {
        SubmitCache submitCache = submitCacheMapper.selectById(id);
        if (submitCache != null) {
            return BeanCopyUtils.convertTo(submitCache, SubCacheVo::new);
        }
        return null;
    }
}
