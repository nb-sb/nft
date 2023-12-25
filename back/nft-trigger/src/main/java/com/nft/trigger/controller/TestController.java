package com.nft.trigger.controller;



import com.nft.common.SendEmail;
import com.nft.domain.support.ipfs.IpfsService;
import com.nft.common.Redis.RedisUtil;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;


@RestController
public class TestController {
//    @Autowired
//    IUserAccountService iUserAccountService;

    @Value("${test.name}")
    String name;

    @Value("${nb.sb}")
    String nbsb;


    @Autowired
    IpfsService ipfsService;
//    @Autowired
//    Htest htest;

    @GetMapping("test")
    public String test() throws Exception {
//        System.out.println(nbsb);
//        List<Object> strings = new ArrayList<>();
//        strings.add("hello");
//        htest.set(strings,"19f847052bfd38df9b8aea2bc2ffb6229f2b6c844390a59a3edb186cf880d0ad");
//        System.out.println(htest.get().getValues());
        return nbsb;
    }

    @GetMapping("testipfs")
    @ResponseBody
    public Object upfileipfs() throws IOException {
        File directory = new File("");//参数为空
        String courseFile = directory.getCanonicalPath() ;
        System.out.println(courseFile);
        String path = "";
        path = courseFile + "\\imgs\\nbsb2.png";
        System.out.println(path);
//        IPFSUtil ipfsUtil = new IPFSUtil();
//        IpfsServiceImpl ipfsUtil = new IpfsServiceImpl();
        String hash = ipfsService.uploadToIpfs(path);
        System.out.println(hash);
        return hash;
    }
    @Autowired
    RedisUtil redisUtil;
    @Resource
    SendEmail sendEmail;
    @GetMapping("test2")
    public String test2() throws Exception {
//        System.out.println(redisUtil.set("testkey", "value", 30));
//        // 创建非国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
// 随机生成非国密公私钥对
        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
        System.out.println(cryptoKeyPair.getHexPrivateKey());
        System.out.println(cryptoKeyPair.getHexPublicKey());
        System.out.println(cryptoKeyPair.getAddress());
        return nbsb;
    }
    @GetMapping("test3")
    public Object test3() throws MessagingException {
////        测试分页查询
//        Search search = new Search();
//        search.setSize(2);
//        search.setCurrent(1);
//        Object o = iUserAccountService.selectUserPage(search);
        //测试验证码逻辑
        sendEmail.sentSimpleMail("123", "456", "3500079813@qq.com");
        return "o";
    }

//    @GetMapping("/login")
//    @ResponseBody
//    public UserResult test(@RequestBody LoginReq loginReq) {
//
//        return iUserAccountService.login(loginReq);
//    }
//    @GetMapping("/register")
//    @ResponseBody
//    public UserResult register(@RequestBody SignReq signReq) {
//        return iUserAccountService.register(signReq);
//    }

//    @GetMapping("test")
//    @ResponseBody
//    public String test2() {
//        QueryWrapper<UserInfo> objectQueryWrapper = new QueryWrapper<>();
//        objectQueryWrapper.eq("id", "1");
//        System.out.println(userInfoMapper.selectOne(objectQueryWrapper));
//        String s = "getUserName.get()";
//        return s;
//    }
}
