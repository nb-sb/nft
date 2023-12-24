package com.nft.domain.user.model.res;

import com.nft.common.Result;

/*
* 登录注册返回类
* */
public class UserResult extends Result {

    private String token;
    public UserResult(String code, String info) {
        super(code, info);
    }

    public UserResult(String code, String info, String token) {
        super(code, info);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
