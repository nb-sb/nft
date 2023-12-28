package com.nft.trigger.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@Validated
public class transferConllection {
    private final HttpServletRequest httpServletRequest;
    //用户转增藏品
    void transfer() {
        //验证藏品是否是自己的
        //检查转移用户地址是否存在
        // 删除自己的藏品并添加到转移用户中
    }
}
