package com.nft.domain.user.model.req;

import com.nft.domain.common.NeedVerification;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.jws.HandlerChain;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/*
* 登录请求对象
* */
@Data
@Accessors(chain = true)
public class LoginReq  {
    @NotNull(message = "the paramter cannot be null")
    @Pattern(regexp = "^[\\w]{6,20}(?<!_)$")
    String username;
    @NotNull(message = "the paramter cannot be null")
    @Pattern(regexp ="^[a-zA-Z0-9!@#$%^&*()_+=\\-.\\/\\[\\]{}]{6,16}$" )
    String password;
    String phone;
    @Email
    String email;

    String code;
    @NotNull
    String codeId;


    public LoginReq() {

    }
}
