package com.nft.domain.user.service.Factory;

import com.nft.domain.user.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserEntityFactoryImpl implements UserEntityFatory{

    @Override
    public UserEntity newInstance(String username, String address, String password, String privatekey, BigDecimal balance, Integer role) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username)
                .setPassword(password)
                .setAddress(address)
                .setPrivatekey(privatekey)
                .setBalance(balance)
                .setRole(role);
        return userEntity;
    }
}
