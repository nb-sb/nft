package com.nft.domain.order.model.vo;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Document(indexName = "conllectioninfovo",createIndex = true)
@Accessors(chain = true)
public class ConllectionInfoVo implements Serializable {
    @Id
    @Field(type = FieldType.Keyword, docValues = true)
    private Integer id;//出售id
    @Field(type = FieldType.Keyword)
    private String hash;
    @Field(type = FieldType.Integer)
    private Integer amount; // 发行量
    @Field(type = FieldType.Keyword)
    private Integer remain; // 剩余数量
    @Field(type = FieldType.Keyword)
    private String auther;
    @Field(type = FieldType.Integer)
    private Integer status;
    @Field(type = FieldType.Text)
    private String ipfsHash;
    @Field(type = FieldType.Text)
    private String path;//图片路径
    @Field(type = FieldType.Text)
    private String present; // 介绍
    @Field(type = FieldType.Keyword)
    private String name;//藏品名称
    @Field(type = FieldType.Double)
    private BigDecimal price; // 价格

    @Field(type = FieldType.Integer)
    private BigDecimal mid; //分类id
}
