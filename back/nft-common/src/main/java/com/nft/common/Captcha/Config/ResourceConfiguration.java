//package com.nft.common.Captcha.Config;
//
//import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
//import cloud.tianai.captcha.common.constant.CommonConstant;
//import cloud.tianai.captcha.generator.common.constant.SliderCaptchaConstant;
//import cloud.tianai.captcha.generator.impl.StandardSliderImageCaptchaGenerator;
//import cloud.tianai.captcha.generator.impl.StandardWordOrderClickImageCaptchaGenerator;
//import cloud.tianai.captcha.resource.ResourceStore;
//import cloud.tianai.captcha.resource.common.model.dto.Resource;
//import cloud.tianai.captcha.resource.common.model.dto.ResourceMap;
//import cloud.tianai.captcha.resource.impl.provider.ClassPathResourceProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//
//import static cloud.tianai.captcha.common.constant.CommonConstant.DEFAULT_TAG;
//import static cloud.tianai.captcha.generator.impl.StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH;
//
///**
// * @Author: 天爱有情
// * @date 2022/7/11 14:22
// * @Description 负责模板和背景图存储的地方
// */
//@Component
//@RequiredArgsConstructor
//public class ResourceConfiguration implements InitializingBean {
//
//    // 该ResourceStore 由 tianai-captcha-springboot-starter 自动装配
//    private final ResourceStore resourceStore;
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//        // todo 如果 ResourceStore 是RedisResourceStore的时候， 下面的方法只初始化一次即可，请勿重复将数据添加到redis缓存中
//
//
//        // 滑块验证码 模板 (系统内置)
//        ResourceMap template1 = new ResourceMap(4);
//        template1.put(SliderCaptchaConstant.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/1/active.png")));
//        template1.put(SliderCaptchaConstant.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/1/fixed.png")));
//        ResourceMap template2 = new ResourceMap(4);
//        template2.put(SliderCaptchaConstant.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/2/active.png")));
//        template2.put(SliderCaptchaConstant.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/2/fixed.png")));
//        // 旋转验证码 模板 (系统内置)
//        ResourceMap template3 = new ResourceMap(4);
//        template3.put(SliderCaptchaConstant.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/3/active.png")));
//        template3.put(SliderCaptchaConstant.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/3/fixed.png")));
//
//        // 1. 添加一些模板
//        resourceStore.addTemplate(CaptchaTypeConstant.SLIDER, template1);
//        resourceStore.addTemplate(CaptchaTypeConstant.SLIDER, template2);
//        resourceStore.addTemplate(CaptchaTypeConstant.ROTATE, template3);
//
//        // (语序点选成语字典，这里只演示一个)
//        resourceStore.addTemplate(CaptchaTypeConstant.WORD_ORDER_IMAGE_CLICK, StandardWordOrderClickImageCaptchaGenerator.wordToResourceMap("鸡你太美", "default"));
//
//        // 添加icon(图标点选)
//        resourceStore.addResource(CommonConstant.IMAGE_CLICK_ICON, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_RESOURCE_PATH.concat("/icon/1.png"), DEFAULT_TAG));
//        resourceStore.addResource(CommonConstant.IMAGE_CLICK_ICON, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_RESOURCE_PATH.concat("/icon/2.png"), DEFAULT_TAG));
//        resourceStore.addResource(CommonConstant.IMAGE_CLICK_ICON, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_RESOURCE_PATH.concat("/icon/3.png"), DEFAULT_TAG));
//        resourceStore.addResource(CommonConstant.IMAGE_CLICK_ICON, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_RESOURCE_PATH.concat("/icon/4.png"), DEFAULT_TAG));
//        resourceStore.addResource(CommonConstant.IMAGE_CLICK_ICON, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_RESOURCE_PATH.concat("/icon/5.png"), DEFAULT_TAG));
//        resourceStore.addResource(CommonConstant.IMAGE_CLICK_ICON, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_RESOURCE_PATH.concat("/icon/6.png"), DEFAULT_TAG));
//        resourceStore.addResource(CommonConstant.IMAGE_CLICK_ICON, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_RESOURCE_PATH.concat("/icon/7.png"), DEFAULT_TAG));
//        resourceStore.addResource(CommonConstant.IMAGE_CLICK_ICON, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_RESOURCE_PATH.concat("/icon/8.png"), DEFAULT_TAG));
//
//        // 2. 添加自定义背景图片
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/a.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/b.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/c.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/d.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/e.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/g.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/h.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/i.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/j.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "bgimages/48.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "bgimages/aaa.png"));
//        resourceStore.addResource(CaptchaTypeConstant.ROTATE_DEGREE, new Resource("classpath", "bgimages/135.png"));
//        resourceStore.addResource(CaptchaTypeConstant.ROTATE_DEGREE, new Resource("classpath", "bgimages/139.png"));
//        resourceStore.addResource(CaptchaTypeConstant.ROTATE_DEGREE, new Resource("classpath", "bgimages/141.png"));
//
//
//        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "bgimages/c.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.IMAGE_CLICK, new Resource("classpath", "bgimages/a.jpg"));
//        resourceStore.addResource(CaptchaTypeConstant.WORD_ORDER_IMAGE_CLICK, new Resource("classpath", "bgimages/BBB.jpeg"));
//    }
//}
