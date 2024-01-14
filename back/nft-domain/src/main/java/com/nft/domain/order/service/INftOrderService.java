package com.nft.domain.order.service;

import com.nft.common.Result;

public interface INftOrderService {

    //查询自己待支付/已支付等状态订单
    Result getOrderByStatus(Integer userId, Integer payOrderStatus);
}
