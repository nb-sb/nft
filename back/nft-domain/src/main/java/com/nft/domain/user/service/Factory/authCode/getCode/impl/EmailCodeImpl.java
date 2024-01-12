package com.nft.domain.user.service.Factory.authCode.getCode.impl;

import cn.hutool.core.util.RandomUtil;
import com.nft.common.Utils.CheckUtils;
import com.nft.domain.email.SendEmailService;
import com.nft.domain.user.service.Factory.authCode.getCode.IGetCodeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Log4j2
public class EmailCodeImpl implements IGetCodeService {
    private SendEmailService sendEmailService;

    public EmailCodeImpl(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }

    @Override
    public String getCode(String target) throws MessagingException {
        if (!CheckUtils.isEmail(target)) {
            return null;
        }
        String code = RandomUtil.randomNumbers(6);//模拟获取验证码操作
        sendEmailService.sendEmailAuthenticat(target, code);
        return code;
    }
}
