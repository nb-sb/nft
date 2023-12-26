package com.nft.infrastructure.repository;

import com.nft.domain.nft.repository.INftRelationshipRespository;
import com.nft.infrastructure.dao.NftRelationshipsMapper;
import com.nft.infrastructure.po.NftRelationships;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class INftRelationshipImpl implements INftRelationshipRespository {

    @Autowired
    NftRelationshipsMapper nftRelationshipsMapper;

    @Override
    public boolean addMetas(Integer cid , Integer mid) {
        NftRelationships nftRelationships = new NftRelationships();
        nftRelationships.setCid(cid).setMid(mid);
        nftRelationshipsMapper.insert(nftRelationships);
        return false;
    }
}
