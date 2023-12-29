package com.nft.domain.user.model.res;

import com.nft.common.Result;
import lombok.Data;

/*
* 登录注册返回类
* */
@Data
public class UserResult extends Result {

    private String token;
    public UserResult(String code, String info) {
        super(code, info);
    }

    public UserResult(String code, String info, String token) {
        super(code, info);
        this.token = token;
    }
    public static UserResult success(String data) {
        return new UserResult("1", "success", data);
    }
    public static UserResult success(String info,String data) {
        return new UserResult("1", info, data);
    }
    public static UserResult error(String info) {
        return new UserResult("0", info);
    }
}
