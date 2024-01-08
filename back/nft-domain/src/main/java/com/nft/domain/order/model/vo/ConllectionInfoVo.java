package com.nft.domain.order.model.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ConllectionInfoVo implements Serializable {

    private Integer id;//出售id
    private String hash;
    private Integer amount; // 发行量
    private Integer remain; // 剩余数量
    private String auther;
    private Integer status;
    private String ipfsHash;
    private String path;//图片路径
    private String present; // 介绍
    private String name;//藏品名称
    private BigDecimal price; // 价格
    private BigDecimal mid; //分类id
}
