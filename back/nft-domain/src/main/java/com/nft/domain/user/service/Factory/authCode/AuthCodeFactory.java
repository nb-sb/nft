package com.nft.domain.user.service.Factory.authCode;

import com.nft.domain.email.SendEmailService;
import com.nft.domain.user.service.Factory.authCode.getCode.IGetCodeService;
import com.nft.domain.user.service.Factory.authCode.getCode.impl.EmailCodeImpl;
import com.nft.domain.user.service.Factory.authCode.getCode.impl.PhoneCodeImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthCodeFactory {
    private final SendEmailService sendEmailService;
    public IGetCodeService getCodeService(Integer type) {
        //3 为获取手机验证码 4 为获取邮箱验证码
        if (3 == type) {
            return new PhoneCodeImpl();
        } else if (4 == type) {
            return new EmailCodeImpl(sendEmailService);
        }
        return null;
    }
}
