package com.nft.domain.order.service;

import com.nft.domain.order.model.entity.OrderEntity;
import com.nft.domain.user.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderEntityFactoryImpl implements OrderEntityFatory {


    @Override
    public OrderEntity newInstance(Integer productId, String productImg, String productName, BigDecimal productPrice, BigDecimal seckillPrice) {
        return new OrderEntity()
                .setProductId(productId)
                .setProductImg(productImg)
                .setProductName(productName)
                .setProductPrice(productPrice)
                .setSeckillPrice(seckillPrice);

    }
}
