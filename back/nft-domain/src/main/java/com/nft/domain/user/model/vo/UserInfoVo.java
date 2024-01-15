package com.nft.domain.user.model.vo;

import com.nft.domain.user.model.entity.UserEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserInfoVo extends UserEntity {
    private String cardId;
    private String phoneNumber;
}
