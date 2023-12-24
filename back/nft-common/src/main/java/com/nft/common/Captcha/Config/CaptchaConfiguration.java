//package com.nft.common.Captcha.Config;
//
//import cloud.tianai.captcha.generator.impl.StaticCaptchaPostProcessorManager;
//import cloud.tianai.captcha.spring.plugins.chain.validators.CaptchaRequestNumInterceptor;
//import cloud.tianai.captcha.spring.plugins.chain.validators.DifferenceChainValidator;
//import cloud.tianai.captcha.spring.store.CacheStore;
//import cloud.tianai.captcha.validator.ImageCaptchaValidator;
//import cloud.tianai.captcha.validator.impl.chain.ChainImageCaptchaValidator;
//import cloud.tianai.captcha.validator.impl.chain.validators.ParamCheckChainValidator;
//import cloud.tianai.captcha.validator.impl.chain.validators.TrackFeaturesGenerator;
//import cloud.tianai.captcha.validator.impl.chain.validators.XGBChainValidator;
//import lombok.SneakyThrows;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.InputStream;
//
//@Configuration
//public class CaptchaConfiguration {
//
//    @Bean
//    @SneakyThrows
//    public ImageCaptchaValidator imageCaptchaValidator(CacheStore cacheStore) {
//        ChainImageCaptchaValidator imageCaptchaValidator = new ChainImageCaptchaValidator();
//        // 参数基本校验
//        imageCaptchaValidator.addValidator(new ParamCheckChainValidator());
//        // 请求次数过多校验
//        CaptchaRequestNumInterceptor captchaRequestNumInterceptor = new CaptchaRequestNumInterceptor(cacheStore);
//        imageCaptchaValidator.addValidator(captchaRequestNumInterceptor);
//        StaticCaptchaPostProcessorManager.add(captchaRequestNumInterceptor);
//        // 预生成轨迹特征
//        imageCaptchaValidator.addValidator(new TrackFeaturesGenerator());
//        // 基于IP的差异校验
//        imageCaptchaValidator.addValidator(new DifferenceChainValidator(cacheStore));
//        // xgboost轨迹校验
//        InputStream inputStream = new ClassPathResource("model/xgb.model").getInputStream();
//        XGBChainValidator xgboost = new XGBChainValidator(inputStream);
//        imageCaptchaValidator.addValidator(xgboost);
//        return imageCaptchaValidator;
//    }
//}
