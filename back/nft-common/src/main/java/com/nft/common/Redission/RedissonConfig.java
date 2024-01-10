package com.nft.common.Redission;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置
 *
 * @author qimu
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;

    private String port;

    private String password;

    @Bean
    public RedissonClient redissonClient() {
        // 1. 创建配置
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        //单节点连接
        config.useSingleServer().setAddress(redisAddress).setPassword(password).setDatabase(0);
        //多节点连接
//        config.useClusterServers()
////                // 集群状态扫描间隔时间，单位是毫秒
//                .setScanInterval(2000)
//                //cluster方式至少6个节点(3主3从，3主做sharding，3从用来保证主宕机后可以高可用)
////                .addNodeAddress("redis://43.153.225.14:6380")
//                .addNodeAddress(redisAddress)
//                .setPassword("123456");
        // 2. 创建实例
        //得到redisson对象
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
