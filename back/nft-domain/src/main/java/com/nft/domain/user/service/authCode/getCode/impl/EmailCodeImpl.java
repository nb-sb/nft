package com.nft.domain.user.service.authCode.getCode.impl;

import cn.hutool.core.util.RandomUtil;
import com.nft.common.Utils.CheckUtils;
import com.nft.domain.email.SendEmailService;
import com.nft.domain.user.model.entity.AuthCodeActionEntity;
import com.nft.domain.user.model.vo.AuthCodeCheckTypeVO;
import com.nft.domain.user.service.authCode.factory.DefaultAuthCodeFactory;
import com.nft.domain.user.service.authCode.getCode.IGetCodeService;
import com.nft.domain.user.service.annotation.LogicStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Log4j2
@Component
@LogicStrategy(logicMode = DefaultAuthCodeFactory.AuthCodeModel.EMAIL) //用于转map对象时获取的内容
public class EmailCodeImpl implements IGetCodeService<AuthCodeActionEntity.AuthEmailEntity> {

    @Autowired
    private SendEmailService sendEmailService;

    @Override
    public AuthCodeActionEntity<AuthCodeActionEntity.AuthEmailEntity> getCode(String target) throws MessagingException {
        if (!CheckUtils.isEmail(target)) {
            return null;
        }
        String code = RandomUtil.randomNumbers(6);//模拟获取验证码操作
        sendEmailService.sendEmailAuthenticat(target, code);
        return AuthCodeActionEntity.<AuthCodeActionEntity.AuthEmailEntity>builder()
                .authModel(DefaultAuthCodeFactory.AuthCodeModel.EMAIL.getType())
                .data(
                        AuthCodeActionEntity.AuthEmailEntity.builder()
                                .code(true)
                                .res(code)
                                .build()
                )
                .code(AuthCodeCheckTypeVO.ALLOW.getCode())
                .code(AuthCodeCheckTypeVO.ALLOW.getInfo())
                .info("ok")
                .build();
    }

}
