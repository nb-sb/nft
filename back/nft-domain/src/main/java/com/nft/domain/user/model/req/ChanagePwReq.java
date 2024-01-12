package com.nft.domain.user.model.req;

import com.nft.domain.common.anno.Status;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ChanagePwReq {

    String username;
    @NotNull
    @Pattern(regexp ="^[a-zA-Z0-9!@#$%^&*()_+=\\-.\\/\\[\\]{}]{6,16}$" )
    String password;
    @Pattern(regexp ="^[a-zA-Z0-9!@#$%^&*()_+=\\-.\\/\\[\\]{}]{6,16}$" )
    String oldpassword;

    @Pattern(regexp = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57]|19[189])[0-9]{8}$")
    String phone;
    @Email
    String email;
    String code;
    @NotNull
    @Status(statusType = {"1","2","3"})
    Integer type; // 验证类型  1 是使用旧密码修改 2 是使用 使用邮箱验证码修改 , 3是使用手机验证码

}
