package com.nft.domain.nftSort.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MetaEntity {
    private Integer mid;
    private String name;

    private String slug;

    private Integer count;

    public void initCount() {
        this.count = 0;
    }
}
