package com.nft.domain.user.service.Factory.authCode.getCode.impl;

import com.nft.common.Utils.CheckUtils;
import com.nft.domain.user.model.entity.AuthCodeActionEntity;
import com.nft.domain.user.service.Factory.authCode.DefaultAuthCodeFactory;
import com.nft.domain.user.service.Factory.authCode.getCode.IGetCodeService;
import com.nft.domain.user.service.annotation.LogicStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@LogicStrategy(logicMode = DefaultAuthCodeFactory.AuthCodeModel.PHONE) //用于转map对象时获取的内容
public class PhoneCodeImpl implements IGetCodeService {

    @Override
    public AuthCodeActionEntity getCode(String target) {
        if (!CheckUtils.isMobile(target)) {
            return null;
        }
        return null;
    }
}
