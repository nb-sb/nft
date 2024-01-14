package com.nft.domain.user.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserInfoVo {
    private Integer id;
    private String username;
    private String address;
    private String password;
    private String privatekey;
    private BigDecimal balance;
    private Integer role;
    private String cardId;
    private String phoneNumber;
}
