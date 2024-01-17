package com.nft.domain.order.service;

import com.nft.domain.order.model.entity.OrderEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderEntityFactoryImpl implements OrderEntityFatory {


    @Override
    public OrderEntity newInstance(Integer productId, String productImg, String productName, BigDecimal productPrice, BigDecimal seckillPrice) {
        return  OrderEntity.builder()
                .productId(productId)
                .productImg(productImg)
                .productName(productName)
                .productPrice(productPrice)
                .seckillPrice(seckillPrice).build();

    }
}
