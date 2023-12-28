package com.nft.domain.nft.model.req;

import lombok.Data;

import java.util.Date;

/**
* @author: 戏人看戏
* @Date: 2023/12/28 8:45
* @Description: 用于查到fisco中的藏品数据后将其数据添加至mysql中的传入参数
*/
@Data
public class AddUserConllection2MysqlReq {
    Date time;//时间
    Integer type;//藏品获得类型
    String digital_collection_id;//藏品编号
    String hash;//藏品存贮在ipfs中的hash

    public static Date timestamp2Date(String s) {
        Date date = new Date(Long.parseLong(s));
        return date;
    }
}
