package com.nft.domain.user.model.req;

import com.nft.domain.support.NeedVerification;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/*
* 登录请求对象
* */
@Data
@Accessors(chain = true)
public class LoginReq   extends NeedVerification{
    @NotNull(message = "the paramter cannot be null")
    @Pattern(regexp = "^[\\w]{6,20}(?<!_)$")
    String username;
    @NotNull(message = "the paramter cannot be null")
    @Pattern(regexp ="^[a-zA-Z0-9!@#$%^&*()_+=\\-.\\/\\[\\]{}]{6,16}$" )
    String password;
    @Pattern(regexp = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57]|19[189])[0-9]{8}$")
    String phone;
    @Email
    String email;
    String code;


    public LoginReq() {

    }
}
