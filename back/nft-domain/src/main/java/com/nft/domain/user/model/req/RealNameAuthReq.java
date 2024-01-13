package com.nft.domain.user.model.req;

import com.nft.domain.common.Aop.IdCard;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class RealNameAuthReq {
    @NotNull
    String name;//姓名
    @NotNull
    @IdCard
    String cardId;//身份证
    @NotNull
    @Pattern(regexp = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57]|19[189])[0-9]{8}$")
    String phoneNumber;//手机号

    String address;
    Integer forId;
}
