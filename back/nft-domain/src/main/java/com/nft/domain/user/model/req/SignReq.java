package com.nft.domain.user.model.req;

import com.nft.domain.common.NeedVerification;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SignReq extends NeedVerification {
    @NotNull(message = "the paramter cannot be null")
    @Pattern(regexp = "^[\\w]{6,20}(?<!_)$")
    String username;

    @NotNull(message = "the paramter cannot be null")
    @Pattern(regexp ="^[a-zA-Z0-9!@#$%^&*()_+=\\-.\\/\\[\\]{}]{6,16}$" )
    String password;

    @Pattern(regexp = "\"^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57]|19[189])[0-9]{8}$\"")
    String phone;
    @Email(message = "email ? you know ???")
    String email;
    @NotNull(message = "the paramter cannot be null")
    String codeId;

    Integer role;
}
