package com.nft.domain.nft.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class DetailInfoVo {

    //藏品hash
    private String hash;

    //转移方用户地址
    private String transferAddress;

    //接受方用户地址
    private String targetAddress;

    //0 表示转增，1表示购买 ||用于藏品来源显示
    private Integer type;

    //时间
    private Date time;

    //数字藏品编号	例如 1#5000 或 51#5000 等也就是id和总数进行拼接
    private String digitalCollectionId;
}
