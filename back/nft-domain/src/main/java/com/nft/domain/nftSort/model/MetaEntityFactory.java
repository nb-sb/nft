package com.nft.domain.nftSort.model;

import com.nft.domain.nftSort.model.entity.MetaEntity;
import com.nft.domain.user.model.entity.UserEntity;

import java.math.BigDecimal;

public interface MetaEntityFactory {
    MetaEntity newInstance(String name,String slug);
}
