package com.nft.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${imgs.path}")
    String videoPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File directory = new File("");//参数为空
        String courseFile = null;
        try {
            courseFile = directory.getCanonicalPath();
        } catch (IOException e) {
            System.out.println(e);
            courseFile = videoPath;
        }
        String path = courseFile + "\\imgs\\";
        registry.addResourceHandler("/imgs/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:"+path);//本地路径
    }
}



