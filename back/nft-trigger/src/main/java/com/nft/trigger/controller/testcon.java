package com.nft.trigger.controller;


import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.user.model.res.UserResult;
import com.nft.infrastructure.fisco.service.OwnershipStorageService;
import com.nft.infrastructure.fisco.service.SellStroageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


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
        return new UserResult("123", "321", "123321");
    }

    @GetMapping("testadmin")
    @AuthPermisson(Constants.permiss.admin)
    public Object testadmin() {
        return null;

    }
    @GetMapping("testuser")
    @AuthPermisson(Constants.permiss.regularUser)
    public Object testuser() {
        return null;

    }
    @GetMapping("alltest")
    @AuthPermisson(Constants.permiss.everyone)
    public Object alltest() {
        return null;

    }
}
