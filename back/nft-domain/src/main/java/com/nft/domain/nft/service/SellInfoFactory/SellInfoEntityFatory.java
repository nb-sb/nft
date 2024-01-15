package com.nft.domain.nft.service.SellInfoFactory;

import com.nft.domain.nft.model.entity.OwnerShipEntity;
import com.nft.domain.nft.model.entity.SellInfoEntity;

public interface SellInfoEntityFatory {
    SellInfoEntity newInstance(Integer uniqueId, String hash, Integer amount, String auther, String ipfsHash);
}
