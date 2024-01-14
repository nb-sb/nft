package com.nft.domain.user.model.vo;

import com.nft.domain.user.model.entity.UserEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class UserVo extends UserEntity {
}
