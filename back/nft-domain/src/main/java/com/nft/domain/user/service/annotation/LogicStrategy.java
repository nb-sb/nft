package com.nft.domain.user.service.annotation;

import com.nft.domain.user.service.Factory.authCode.DefaultAuthCodeFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 策略自定义枚举
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {

    DefaultAuthCodeFactory.AuthCodeModel logicMode();

}
