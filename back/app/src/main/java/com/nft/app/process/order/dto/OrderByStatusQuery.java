package com.nft.app.process.order.dto;

import lombok.Data;

@Data
public class OrderByStatusQuery {
    Integer userId;
    Integer payOrderStatus;

    public OrderByStatusQuery(Integer userId, Integer payOrderStatus) {
        this.userId = userId;
        this.payOrderStatus = payOrderStatus;
    }
}
