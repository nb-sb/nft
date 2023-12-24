package com.nft.infrastructure.repository;

import com.nft.domain.nft.repository.NftRelationshipRespository;
import com.nft.infrastructure.dao.NftRelationshipsMapper;
import com.nft.infrastructure.po.NftRelationships;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NftRelationshipImpl implements NftRelationshipRespository {

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
