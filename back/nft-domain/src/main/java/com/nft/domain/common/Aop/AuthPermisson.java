package com.nft.domain.common.Aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//定义注解用在方法上
@Target({ElementType.METHOD})
//定义保留策略，运行时使用
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPermisson {
    int value() default 11; //默认 everyone
}

