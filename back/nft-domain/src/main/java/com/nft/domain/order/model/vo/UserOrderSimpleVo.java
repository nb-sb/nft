package com.nft.domain.order.model.vo;

import lombok.Data;

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
}
