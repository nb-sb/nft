package com.nft.trigger.controller;

import com.nft.common.Result;
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
    public Result transfer(@Valid @RequestBody TransferReq transferReq) {
        return iNftTransferService.transferCollection(transferReq, httpServletRequest);
    }
}
