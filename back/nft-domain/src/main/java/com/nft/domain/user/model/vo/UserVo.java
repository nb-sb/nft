package com.nft.domain.user.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserVo {
    private Integer id;

    private String address;

    private String username;

    private String password;
    private BigDecimal balance;
    private Integer role;
}
