package com.nft.domain.user.model.vo;

import com.nft.domain.common.Aop.IdCard;
import com.nft.domain.user.model.req.RealNameAuthCmd;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
public class RealNameAuthVo {
    String name;//姓名
    String cardId;//身份证
    String phoneNumber;//手机号
    String address;
    Integer forId;
    Integer status;
}
