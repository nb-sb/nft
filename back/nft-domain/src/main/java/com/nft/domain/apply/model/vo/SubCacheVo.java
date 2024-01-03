package com.nft.domain.apply.model.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class SubCacheVo {
    private Integer id;

    private String path;

    private Integer total;

    private String present;

    private String name;

    private String authorId;

    private String authorAddress;

    private BigDecimal price;

    private Integer status;

    private String hash;
}
