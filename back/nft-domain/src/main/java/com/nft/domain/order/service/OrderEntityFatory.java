package com.nft.domain.order.service;

import com.nft.domain.order.model.entity.OrderEntity;
import com.nft.domain.user.model.entity.UserEntity;

import java.math.BigDecimal;
import java.util.Date;

public interface OrderEntityFatory {
    OrderEntity newInstance(Integer productId, String productImg, String productName, BigDecimal productPrice, BigDecimal seckillPrice);
}
