package com.nft.trigger.controller;

import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.nft.model.req.TransferReq;
import com.nft.domain.nft.service.INftTransferService;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Validated
public class transferConllection {
    private final HttpServletRequest httpServletRequest;
    private final INftTransferService iNftTransferService;
    //用户转增藏品
    @PostMapping("transferConllection")
    @ResponseBody
    @AuthPermisson
    void transfer(@Valid @RequestBody TransferReq transferReq) {
        //验证藏品是否是自己的
        //检查转移用户地址是否存在
        //修改藏品所属用户地址等
        //调用区块链中转移方法
        iNftTransferService.transferCollection(transferReq, httpServletRequest);
    }
}
