package com.nft.trigger.controller;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.common.response.ApiResponseStatusConstant;
import cloud.tianai.captcha.generator.common.model.dto.GenerateParam;
import cloud.tianai.captcha.generator.impl.processor.ShuffleImageProcessor;
import cloud.tianai.captcha.spring.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.vo.CaptchaResponse;
import cloud.tianai.captcha.spring.vo.ImageCaptchaVO;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nft.common.Captcha.Utils.AesUtils;
import com.nft.common.Captcha.Utils.RsaUtils;
import com.nft.common.Result;
import com.nft.domain.support.redis.RedisUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@AllArgsConstructor
public class CaptchaController {
    private ImageCaptchaApplication captchaApplication;
    RedisUtil redisUtil;

    // rsa私钥
    public static final String PRIVATE_KEY =
            "MIICWwIBAAKBgQC0nf3XQxOCL59bUEno36+d0PoGu82WDaeN6vYzlO5p5JOttGIuw0BLjXjJhpKrwU41tmYVygr+t3+y2GAgavEy2R+RSWCYgLEwpi8xjeV5ZfAWORYILF2YuZsG+276HapgQYxajzUlnHrO+En6LJ9sUglg9VTQNnawwKD6/cy8IQIDAQABAoGAWaBg21X8s0iAcmaYFogRdGXD/F1rOj/SWnky7QvFdySnJ7ZwVjYrjJJKUu6Fj9dfwQWfCMb2x8k1BAGdSdjnkPeCXj1+AvvMRzgTJTgRtl355N/AUsyekHnsYysYiDEIfahRP1BztpYR2K+Z+JGdQy+40TNfhNnkRT+FN71WZ7ECQQDZs75ysJFsCqr1TR7iTHtfXGAbwzIDHIy53dFQW6jehL7JgXA6uoRNYs8eqTNTixnQKux1CWKM4WdoBTYA4n+1AkEA1GQeeOqTGzy1eGBNqwQBqDUV0LSXstXO3s3tv1L9in2n8u49vAZi6yI+Gt1ZZLITUfgD+shaR1t/jaEm2qfWPQJAPlm+h/aOD9WWVR87YzDofM1mMU1ce4unumwPUOaPnPuD8Q6DaC6XSdqs22k2bA0A3aANjQ+dDCBaj24+o047qQJAWGbrwAxy+m1EYzxCV6ItRmQCFoJ0eb1Ag8BW6sGSQmiW1SiQjYhi99ei3e+v25e7Luz6SxHFcdoFt1+SGoStSQJAUhmOVRdbbfPk6sdxxcaxa3+dN6JdertI/91AcSrgOEKoE86yr980Njpm75Lj9NsjR8PasLLCkr04PV1Gmqr6CA==";

    @RequestMapping("/gen")
    @ResponseBody
    public CaptchaResponse<ImageCaptchaVO> genCaptcha(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      @RequestParam(value = "type", required = false) String type,
                                                      @RequestParam(value = "tag1", required = false) String tag1,
                                                      @RequestParam(value = "tag2", required = false) String tag2,
                                                      @RequestBody(required = false) EncData data
    ) {
        if (StringUtils.isBlank(type)) {
            type = CaptchaTypeConstant.SLIDER;
        }
        if (data != null) {
            decode(data,  request);
        }

        GenerateParam param = new GenerateParam();
        param.setType(type);
        param.setBackgroundImageTag(tag1);
        param.setTemplateImageTag(tag2);
        // 扩展功能， 是否将生成图片随机打乱
        param.addParam(ShuffleImageProcessor.SHUFFLE_IMAGE_KEY, true);

        CaptchaResponse<ImageCaptchaVO> responseData = captchaApplication.generateCaptcha(param);
        System.out.println("生成的id：" + responseData.getId());
//        将其key vale 保存至redis中30秒过期 redis  key为id value为false
        redisUtil.set(responseData.getId(), false, 30);
        return responseData;
    }

    @RequestMapping("/gen/random")
    @ResponseBody
    public CaptchaResponse<ImageCaptchaVO> genSliderCaptchaRandom(HttpServletRequest request, HttpServletResponse response,
                                                                  @RequestBody(required = false) EncData data) throws IOException {
        int i = ThreadLocalRandom.current().nextInt(0, 6);
        String type;
        if (i == 0) {
            type = CaptchaTypeConstant.SLIDER;
        } else if (i == 1) {
            type = CaptchaTypeConstant.CONCAT;
        } else if (i == 2) {
            type = CaptchaTypeConstant.ROTATE;
        } else if (i == 3) {
            type = CaptchaTypeConstant.ROTATE_DEGREE;
        } else if (i == 4) {
            type = CaptchaTypeConstant.IMAGE_CLICK;
        } else if (i == 5) {
            type = CaptchaTypeConstant.WORD_IMAGE_CLICK;
        } else {
            type = CaptchaTypeConstant.WORD_ORDER_IMAGE_CLICK;
        }

        return genCaptcha( request, response, type, null, null, data);
    }


    @PostMapping("/check3")
    @ResponseBody
    public ApiResponse<?> checkCaptcha3(@RequestBody EncData data,
                                        HttpServletRequest request) {
        ImageCaptchaTrack sliderCaptchaTrack;
        try {

            sliderCaptchaTrack = decode(data, request);
        } catch (Exception e) {
            System.out.println("失败");
            // 解密失败
            return ApiResponse.ofMessage(ApiResponseStatusConstant.NOT_VALID_PARAM);
        }
        String id = data.getId();
        System.out.println("成功 id: "+ id);
//        将redis中的key设置为ture,并加长30秒
        redisUtil.set(id, true, 30);
        ApiResponse<String> matching = (ApiResponse<String>) captchaApplication.matching(id, sliderCaptchaTrack);
        matching.setData(id);
        return matching;

    }

    public ImageCaptchaTrack decode(EncData data, HttpServletRequest request) {
        String ki = data.getKi();
        if (ki == null) {
            return null;
        }
        // todo 你的私钥
        String privateKey = PRIVATE_KEY;
        RSAPrivateKey key = RsaUtils.getPrivateKeyFromPemPKCS1(privateKey);
        byte[] keyAndIv = RsaUtils.decrypt(key, Base64.getDecoder().decode(ki));
        String[] split = new String(keyAndIv).split("\\|");
        String aesKkey = split[0];
        String aesIv = split[1];
        byte[] aesKeyBytes = Hex.decode(aesKkey);
        byte[] aesIvBytes = Hex.decode(aesIv);

        if (data.getCustom() != null) {
            byte[] decode = Base64.getDecoder().decode(data.getCustom());
            byte[] decryptData = AesUtils.decrypt(aesKeyBytes, aesIvBytes, decode);
            Map<String, Map<String, String>> custom = new Gson().fromJson(new String(decryptData), new TypeToken<Map<String, Map<String, String>>>() {
            }.getType());
            Map<String, String> session = custom.get("session");
            if (session != null) {
                session.forEach((k, v) -> request.getSession().setAttribute("captcha_" + k, v));
            }
        }

        if (data.getData() != null) {
            byte[] decode = Base64.getDecoder().decode(data.getData());
            byte[] decryptData = AesUtils.decrypt(aesKeyBytes, aesIvBytes, decode);
            ImageCaptchaTrack sliderCaptchaTrack = new Gson().fromJson(new String(decryptData), ImageCaptchaTrack.class);
            return sliderCaptchaTrack;
        }
        return null;
    }

    @lombok.Data
    public static class EncData {
        private String id;
        private String data;
        private String ki;
        private String custom;
    }

}
