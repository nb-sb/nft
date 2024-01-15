package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.apply.model.entity.SubmitSellEntity;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.infrastructure.dao.SellInfoMapper;
import com.nft.infrastructure.dao.SubmitCacheMapper;
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

    @Override
    public SubmitSellEntity selectOneByHash(String hash) {
        QueryWrapper<SubmitCache> submitWrapper = new QueryWrapper<>();
        submitWrapper.eq("hash",hash);
        SubmitCache submitCache1 = submitCacheMapper.selectOne(submitWrapper);
        SubmitSellEntity subCacheVo = BeanCopyUtils.convertTo(submitCache1, SubmitSellEntity::new);
        return subCacheVo;
    }
    @Override
    public Integer selectIdByHash(String hash) {
        LambdaQueryWrapper<SubmitCache> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubmitCache::getHash, hash)
                .select(SubmitCache::getId);
        SubmitCache submitCache1 = submitCacheMapper.selectOne(queryWrapper);
        return submitCache1.getId();
    }
    @Override
    @Transactional
    public boolean creat(SubmitSellEntity submitSellEntity) {
        SubmitCache submitCache = new SubmitCache();
        submitCache.setId(submitSellEntity.getId());
        submitCache.setPath(submitSellEntity.getPath());
        submitCache.setTotal(submitSellEntity.getTotal());
        submitCache.setPresent(submitSellEntity.getPresent());
        submitCache.setName(submitSellEntity.getName());
        submitCache.setAuthorId(submitSellEntity.getAuthorId());
        submitCache.setAuthorAddress(submitSellEntity.getAuthorAddress());
        submitCache.setPrice(submitSellEntity.getPrice());
        submitCache.setStatus(submitSellEntity.getStatus());
        submitCache.setHash(submitSellEntity.getHash());
        int insert = submitCacheMapper.insert(submitCache);
        return insert > 0;
    }

    @Override
    public boolean upDateSubStatus(SubmitSellEntity submitSellEntity) {
        SubmitCache submitCache = new SubmitCache();
        submitCache.setId(submitSellEntity.getId());
        submitCache.setStatus(submitSellEntity.getStatus());
        int update = submitCacheMapper.updateById(submitCache);
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
    public SubmitSellEntity selectById(Integer id) {
        SubmitCache submitCache = submitCacheMapper.selectById(id);
        if (submitCache != null) {
            return BeanCopyUtils.convertTo(submitCache, SubmitSellEntity::new);
        }
        return null;
    }
}
