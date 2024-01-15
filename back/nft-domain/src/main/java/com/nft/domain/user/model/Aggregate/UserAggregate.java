package com.nft.domain.user.model.Aggregate;

import com.nft.domain.user.model.entity.UserEntity;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UserAggregate {
    private UserEntity userEntity;

    // 构造函数等省略

    // 增加用户的业务逻辑
    public void addUser(String username, String address, String password, String privatekey, BigDecimal balance, Integer role) {
        // 可以在这里添加一些业务规则的校验

        UserEntity newUser = new UserEntity()
                .setUsername(username)
                .setAddress(address)
                .setPassword(password)
                .setPrivatekey(privatekey)
                .setBalance(balance)
                .setRole(role);

        // 可以在这里触发领域事件，进行一些领域逻辑

        this.userEntity = newUser;
    }

    // 其他用户相关操作
    public UserEntity getUserEntity() {
        return this.userEntity;
    }
}
