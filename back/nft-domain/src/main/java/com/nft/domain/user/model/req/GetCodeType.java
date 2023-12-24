package com.nft.domain.user.model.req;

import com.nft.domain.common.anno.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetCodeType {
    @NotNull
    @Status(statusType = {"3","4"})//3 为获取手机验证码 4 为获取邮箱验证码
    Integer type;
    @NotNull
    String name;
    @NotNull
    String codeId;

}
