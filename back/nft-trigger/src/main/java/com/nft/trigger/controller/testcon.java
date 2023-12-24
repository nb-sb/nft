package com.nft.trigger.controller;


import com.nft.common.Result;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.user.model.res.UserResult;
import com.nft.infrastructure.fisco.model.bo.OwnershipStorageAddOwnershipInputBO;
import com.nft.infrastructure.fisco.model.bo.OwnershipStorageSelectByUserAddrInputBO;
import com.nft.infrastructure.fisco.model.bo.SellStroageCreateSellInputBO;
import com.nft.infrastructure.fisco.service.OwnershipStorageService;
import com.nft.infrastructure.fisco.service.SellStroageService;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;


@RestController
public class testcon {

    @Autowired
    OwnershipStorageService ownershipStorageService;
    @Autowired
    SellStroageService sellStroageService;

    @GetMapping("test123")
    public Object test() throws Exception {
//        添加hash 也就是添加藏品
//
//        SellStroageCreateSellInputBO sellStroageCreateSellInputBO = new SellStroageCreateSellInputBO();
//        sellStroageCreateSellInputBO.set_hash("hash123");
//        sellStroageCreateSellInputBO.setAmount(BigInteger.valueOf(10));
//        sellStroageService.createSell(sellStroageCreateSellInputBO);
//
//
//        OwnershipStorageAddOwnershipInputBO ownershipStorageAddOwnershipInputBO = new OwnershipStorageAddOwnershipInputBO();
//        ownershipStorageAddOwnershipInputBO.set_address("0xb206fa9d03aaf789f1edbe1d1947f552d2e91035");
//        ownershipStorageAddOwnershipInputBO.set_hash("hash123");
//        System.out.println("add: "+ownershipStorageService.addOwnership(ownershipStorageAddOwnershipInputBO).getValues());
//
//        OwnershipStorageSelectByUserAddrInputBO ownershipStorageSelectByUserAddrInputBO = new OwnershipStorageSelectByUserAddrInputBO();
//        ownershipStorageSelectByUserAddrInputBO.set_address("0xb206fa9d03aaf789f1edbe1d1947f552d2e91035");
//        CallResponse callResponse = ownershipStorageService.selectByUserAddr(ownershipStorageSelectByUserAddrInputBO);
//        System.out.println(callResponse.getValues());
//        return callResponse.getValues();
        return null;
    }

    @GetMapping("test1234")
    public Result test123321() {
        return new UserResult("123","321","123321");
    }
}
