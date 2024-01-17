package com.nft.domain.user.service.Factory;

import com.nft.domain.user.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserEntityFactoryImpl implements UserEntityFatory{

    @Override
    public UserEntity newInstance(String username, String address, String password, String privatekey, BigDecimal balance, Integer role) {
        return UserEntity.builder()
                .username(username)
                .password(password)
                .address(address)
                .privatekey(privatekey)
                .balance(balance)
                .role(role).build();
    }
}
