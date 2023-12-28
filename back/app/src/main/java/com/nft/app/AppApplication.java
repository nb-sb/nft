package com.nft.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: spring boot 启动类
 * @author：戏人看戏，微信：whn3500079813
 * @date: 2023/12/7
 * @Copyright：网站： https://nb.sb/ || git开源地址：https://gitee.com/nb-sb/nft
 */
@SpringBootApplication
@ComponentScan("com.nft.*")
@MapperScan("com.nft.infrastructure.dao")
public class AppApplication  {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }




}
