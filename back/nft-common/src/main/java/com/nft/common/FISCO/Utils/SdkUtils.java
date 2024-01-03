package com.nft.common.FISCO.Utils;

import org.elasticsearch.cluster.coordination.PublicationTransportHandler;
import org.fisco.bcos.sdk.BcosSDK;
import org.springframework.stereotype.Service;


@Service
public class SdkUtils {
    // 获取配置文件路径
    public String configFile = SdkUtils.class.getClassLoader().getResource("config.toml").getFile();

    public BcosSDK sdk =  BcosSDK.build(configFile);
}
