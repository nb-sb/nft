package com.nft.domain.user.service.Factory.authCode;


import com.nft.domain.user.model.entity.AuthCodeActionEntity;
import com.nft.domain.user.service.Factory.authCode.getCode.IGetCodeService;
import com.nft.domain.user.service.annotation.LogicStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 规则工厂
 * @create 2023-12-31 11:23
 */
@Service
public class DefaultAuthCodeFactory {

    public Map<String, IGetCodeService<?>> AuthMap = new ConcurrentHashMap<>();
    //进行初始化各种规则 使用规则标识符对应着规则 泛型注入
    public DefaultAuthCodeFactory(List<IGetCodeService<?>> iGetCodeServices) {
        for (IGetCodeService<?> iGetCodeService : iGetCodeServices) {
            //获取注解上面参数信息
            LogicStrategy strategy = AnnotationUtils.findAnnotation(iGetCodeService.getClass(), LogicStrategy.class);
            if (null != strategy) {
                AuthMap.put(strategy.logicMode().getType(), iGetCodeService);
            }
        }
    }

    public <T extends AuthCodeActionEntity.AuthEntity> Map<String, IGetCodeService<T>> openLogicAuth() {
        return (Map<String, IGetCodeService<T>>) (Map<?, ?>) AuthMap;
    }

    @Getter
    @AllArgsConstructor
    public enum AuthCodeModel {

        EMAIL("0000", "邮箱验证", "email"),
        PHONE("0001", "手机号验证", "phone"),
        ;

        private final String code;
        private final String info;
        private final String type;


    }

}
