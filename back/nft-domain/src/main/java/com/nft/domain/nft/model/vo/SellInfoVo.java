package com.nft.domain.nft.model.vo;

import lombok.Data;

@Data
public class SellInfoVo {
    private Integer id;

    private Integer uniqueId;

    private String hash;

    private Integer amount;

    private Integer remain;

    private String auther;

    private Integer status;
    private String ipfsHash;
}
