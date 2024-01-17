package com.nft.domain.order.model.entity;

import com.nft.common.Constants;
import com.nft.common.Utils.OrderNumberUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    private Integer id;
    private String orderNo;

    private Integer userId;

    private Integer productId;


    private String productImg;


    private String productName;


    private BigDecimal productPrice;


    private BigDecimal seckillPrice;

    private Integer status;

    private Date payDate;

    private Date initDate;

    public void initOrder(Integer userid,Date initDate) {
        this.status = Constants.payOrderStatus.NO_PAY;
        this.payDate = null;
        this.userId = userid;
        this.orderNo = OrderNumberUtil.generateOrderNumber(this.userId, this.productId);
        this.initDate = initDate;
    }
}
