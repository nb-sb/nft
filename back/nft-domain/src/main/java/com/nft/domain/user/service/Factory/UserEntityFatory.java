package com.nft.domain.user.service.Factory;

import com.nft.domain.user.model.entity.UserEntity;

import java.math.BigDecimal;

public interface UserEntityFatory {
    UserEntity newInstance(String username, String address, String password, String privatekey, BigDecimal balance,Integer role);
}
