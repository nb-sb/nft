package com.nft.infrastructure.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderInfo {
    /**
     * 订单id
     */
    private String orderNo;
    /**
     * 用户id
     * */
    private Integer userId;
    /**
     *商品id
     */
    private Integer productId;

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
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 0是创建完成订单，1是未支付，2是已支付，3是藏品已经到账，4是取消订单，5是已经退款
     */
    private Integer status;

    /**
     * 支付时间
     */
    private Date payDate;
}
