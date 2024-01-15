package com.nft.domain.nft.service.SellInfoFactory;

import com.nft.domain.nft.model.entity.SellInfoEntity;
import org.springframework.stereotype.Service;

@Service
public class SellInfoEntityFactoryImpl implements SellInfoEntityFatory {

    @Override
    public SellInfoEntity newInstance(Integer uniqueId, String hash, Integer amount, String auther, String ipfsHash) {
        return new SellInfoEntity().setUniqueId(uniqueId)
                .setHash(hash)
                .setAmount(amount)
                .setAuther(auther)
                .setIpfsHash(ipfsHash);
    }
}
