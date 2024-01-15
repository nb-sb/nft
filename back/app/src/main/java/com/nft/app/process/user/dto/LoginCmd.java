package com.nft.app.process.user.dto;

import com.nft.domain.user.model.req.LoginReq;
import lombok.Data;
import lombok.experimental.Accessors;
/*
 * 登录请求cmd
 * */
@Data
@Accessors(chain = true)
public class LoginCmd extends LoginReq {

}
