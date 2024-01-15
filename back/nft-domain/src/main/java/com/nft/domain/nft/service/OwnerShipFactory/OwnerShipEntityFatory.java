package com.nft.domain.nft.service.OwnerShipFactory;

import com.nft.domain.nft.model.entity.OwnerShipEntity;

public interface OwnerShipEntityFatory {
    OwnerShipEntity newInstance(String address,Integer type,String digital_collection_id,String hash);
}
