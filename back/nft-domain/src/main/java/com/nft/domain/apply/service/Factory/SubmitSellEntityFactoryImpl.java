package com.nft.domain.apply.service.Factory;

import com.nft.domain.apply.model.entity.SubmitSellEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SubmitSellEntityFactoryImpl implements SubmitSellEntityFatory {

    @Override
    public SubmitSellEntity newInstance(String path, Integer total, String present, String name, BigDecimal price, String hash, String authorId, String authorAddress) {
        return SubmitSellEntity.builder()
                .path(path)
                .total(total)
                .present(present)
                .name(name)
                .price(price)
                .authorId(authorId)
                .authorAddress(authorAddress)
                .hash(hash).build();
    }
}
