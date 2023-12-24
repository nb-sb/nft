package com.nft.domain.user.model.vo;

import lombok.Data;

@Data
public class UserVo {
    private Integer id;

    private String address;

    private String username;

    private String password;
    private Integer role;
}
