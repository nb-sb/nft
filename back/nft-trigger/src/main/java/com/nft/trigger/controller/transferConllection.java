package com.nft.trigger.controller;

import com.nft.domain.nft.service.INftTransferService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@Validated
public class transferConllection {
    private final HttpServletRequest httpServletRequest;
    private final INftTransferService iNftTransferService;
    //用户转增藏品
    void transfer() {
        //验证藏品是否是自己的
        //检查转移用户地址是否存在
        //修改藏品所属用户地址等
        //调用区块链中转移方法
        iNftTransferService.transferCollection();
    }
}
