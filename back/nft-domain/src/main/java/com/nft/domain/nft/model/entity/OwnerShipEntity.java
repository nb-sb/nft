package com.nft.domain.nft.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@Accessors(chain = true)
public class OwnerShipEntity {
    private String address;
    private Date time;//时间
    private Integer type;//藏品获得类型
    private String digital_collection_id;//藏品编号
    private String hash;//藏品存贮在ipfs中的hash

    public void timestamp2Date(String s) {
        Date date = new Date(Long.parseLong(s));
        this.time = date;
    }
}
