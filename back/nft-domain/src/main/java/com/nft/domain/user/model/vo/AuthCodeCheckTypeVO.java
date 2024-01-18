package com.nft.domain.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
* @author: 戏人看戏
* @Date: 2024/1/18 19:52
* @Description: 验证码校验类型值对象
*/
@Getter
@AllArgsConstructor
public enum AuthCodeCheckTypeVO {

    ALLOW("0000", "验证成功"),
    REJECT("0001","验证失败"),
    ;

    private final String code;
    private final String info;

}
