//package com.nft;
//
//
//import com.nft.app.AppApplication;
//import com.nft.common.FISCO.Utils.SdkUtils;
//import com.nft.domain.user.model.req.LoginReq;
//import com.nft.domain.user.repository.IUserInfoRepository;
//import org.fisco.bcos.sdk.BcosSDK;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.io.IOException;
//
//@SpringBootTest
//@ContextConfiguration(classes = {AppApplication.class, User.class})
//public class User {
//    @Autowired
//    IUserInfoRepository iUserInfoRepository;
//    @Test
//    public void test() {
//        LoginReq loginReq = new LoginReq();
//        loginReq.setUsername("123");
//        loginReq.setPassword("123");
//        System.out.println(iUserInfoRepository.selectOne(loginReq));
//        System.out.println("123");
//    }
//
//    @Autowired
//    SdkUtils sdkUtils;
//    @Test
//    public void test2() throws IOException {
//        BcosSDK build = sdkUtils.build();
//
//    }
//}
