package com.nft.domain.nftSort.model;

import com.nft.domain.nftSort.model.entity.MetaEntity;
import com.nft.domain.user.model.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class MetaEntityFactoryImpl implements MetaEntityFactory{
    @Override
    public MetaEntity newInstance(String name, String slug) {
        MetaEntity meta = new MetaEntity();
        meta.setName(name)
                .setSlug(slug);
        return meta;
    }
}
