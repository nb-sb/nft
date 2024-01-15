package com.nft.domain.apply.service.Factory;

import com.nft.domain.apply.model.entity.SubmitSellEntity;

import java.math.BigDecimal;

public interface SubmitSellEntityFatory {
    SubmitSellEntity newInstance(String path,
                                 Integer total,
                                 String present,
                                 String name,
                                 BigDecimal price,
                                 String hash,
                                 String authorId,
                                 String authorAddress
                                 );
}
