package com.nft.app.order.dto;

import lombok.Data;

@Data
public class PayOrderCmd {
    private Integer userId;
    private String userAddress;
    private String OrderNumber;
    private Integer paytype;

    public PayOrderCmd(Integer userId, String userAddress, String orderNumber, Integer paytype) {
        this.userId = userId;
        this.userAddress = userAddress;
        OrderNumber = orderNumber;
        this.paytype = paytype;
    }
}
