package com.nft.domain.apply.service.Factory;

import com.nft.domain.apply.model.entity.SubmitSellEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SubmitSellEntityFactoryImpl implements SubmitSellEntityFatory {

    @Override
    public SubmitSellEntity newInstance(String path, Integer total, String present, String name, BigDecimal price, String hash, String authorId, String authorAddress) {
        return new SubmitSellEntity()
                .setPath(path)
                .setTotal(total)
                .setPresent(present)
                .setName(name)
                .setPrice(price)
                .setAuthorId(authorId)
                .setAuthorAddress(authorAddress)
                .setHash(hash);
    }
}
