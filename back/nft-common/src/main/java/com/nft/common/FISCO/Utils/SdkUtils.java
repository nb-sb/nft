package com.nft.common.FISCO.Utils;

import com.nft.common.FISCO.Config.SdkBeanConfig;
import org.fisco.bcos.sdk.BcosSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class SdkUtils {
    private final SdkBeanConfig sdkBeanConfig;

    @Autowired
    public SdkUtils(SdkBeanConfig sdkBeanConfig) {
        this.sdkBeanConfig = sdkBeanConfig;
    }

    public BcosSDK sdk;

    @PostConstruct
    public void init() {
        String configFile = sdkBeanConfig.getConfigFilePath();
        sdk = BcosSDK.build(configFile);
    }
    // 获取配置文件路径
//    public String configFile = SdkUtils.class.getClassLoader().getResource("config.toml").getFile();
//    public String configFile = "D:\\myProject\\NFT\\back\\app\\src\\main\\resources\\config.toml" ;
//
//    public BcosSDK sdk =  BcosSDK.build(configFile);
}
