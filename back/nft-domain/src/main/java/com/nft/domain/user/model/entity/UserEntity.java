package com.nft.domain.user.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@Accessors(chain = true)
public class UserEntity {
    private String username;
    private String address;
    private String password;
    private String privatekey;
    private BigDecimal balance;
    private Integer role;
}
