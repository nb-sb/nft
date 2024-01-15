package com.nft.domain.user.model.vo;

import lombok.Data;

@Data
public class UserDetalVo {
    private Integer forId;

    private String name;

    private String address;

    private String email;
    private String cardid;

    private String phoneNumber;

    private Integer status;
}
