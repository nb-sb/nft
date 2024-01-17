package com.nft.domain.nft.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SellInfoEntity {
    private Integer id;

    private Integer uniqueId;

    private String hash;//藏品hash

    private Integer amount;//发行量

    private Integer remain;//剩余数量

    private String auther;//数字藏品作者地址

    private Integer status;//# 1 为正常 ，  0 为闭售

    private String ipfsHash;
    public void init() {
        this.status = 1;
        this.remain = this.amount;
    }

}
