package com.nft.common.ElasticSearch;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(indexName = "userordersimple")
@Accessors(chain = true)
public class UserOrderSimpleES {
    /**
     * 所属用户id
     */

    @Field(type = FieldType.Keyword)
    private Integer userId;
    /**
     * 订单id
     */
    @Id
    @Field(type = FieldType.Keyword)
    private String orderNo;
    /**
     *商品图片
     */
    @Field(type = FieldType.Keyword)
    private String productImg;
    /**
     *商品名称
     */
    @Field(type = FieldType.Keyword)
    private String productName;
    /**
     * 产品价格
     */
    @Field(type = FieldType.Double)
    private BigDecimal productPrice;
    /**
     * 订单创建时间
     */
    @Field(type = FieldType.Long)
    private Date initDate;
    /**
     * 订单状态
     */
    @Field(type = FieldType.Long)
    private Integer status;
}
