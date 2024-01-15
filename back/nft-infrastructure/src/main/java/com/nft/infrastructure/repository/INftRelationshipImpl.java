package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nft.domain.nftSort.model.entity.MetaRelationShipEntity;
import com.nft.domain.nftSort.repository.INftRelationshipRespository;
import com.nft.infrastructure.dao.NftRelationshipsMapper;
import com.nft.infrastructure.po.NftRelationships;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class INftRelationshipImpl implements INftRelationshipRespository {

    @Autowired
    NftRelationshipsMapper nftRelationshipsMapper;

    @Override
    public boolean creat(MetaRelationShipEntity metaRelationShipEntity) {
        NftRelationships nftRelationships = new NftRelationships();
        nftRelationships.setCid(metaRelationShipEntity.getCid());
        nftRelationships.setMid(metaRelationShipEntity.getMid());
        int insert = nftRelationshipsMapper.insert(nftRelationships);
        if (insert>0) return true;
        return false;
    }

    @Override
    public Integer loadMid(Integer cid) {
        LambdaQueryWrapper<NftRelationships> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NftRelationships::getCid, cid).select(NftRelationships::getMid);
        NftRelationships nftRelationships = nftRelationshipsMapper.selectOne(queryWrapper);
        return nftRelationships.getMid();
    }
}
