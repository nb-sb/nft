package com.nft.domain.nft.service.OwnerShipFactory;

import com.nft.domain.nft.model.entity.OwnerShipEntity;
import org.springframework.stereotype.Service;

@Service
public class OwnerShipEntityFactoryImpl implements OwnerShipEntityFatory {


    @Override
    public OwnerShipEntity newInstance(String address, Integer type, String digital_collection_id, String hash) {
        return OwnerShipEntity.builder()
                .type(type)
                .digital_collection_id(digital_collection_id)
                .hash(hash)
                .address(address).build();
    }
}
