package com.nft.domain.order.model.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserOrderSimpleVo {
    /**
     * 订单id
     */
    private String orderNo;
    /**
     *商品图片
     */
    private String productImg;
    /**
     *商品名称
     */
    private String productName;
    /**
     * 产品价格
     */
    private BigDecimal productPrice;
    /**
     * 订单创建时间
     */
    private Date initDate;
    /**
     * 订单状态
     */
    private Integer status;
}
