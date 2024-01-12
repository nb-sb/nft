package com.nft.domain.user.service.Factory.authCode;

import com.nft.domain.email.SendEmailService;
import com.nft.domain.user.service.Factory.authCode.getCode.IGetCodeService;
import com.nft.domain.user.service.Factory.authCode.getCode.impl.EmailCodeImpl;
import com.nft.domain.user.service.Factory.authCode.getCode.impl.PhoneCodeImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
@Service
public class AuthCodeService {
    @Autowired
    private SendEmailService sendEmailService;
    private IGetCodeService getCodeService;

    public AuthCodeService AuthCodeService(Integer type) {
        //3 为获取手机验证码 4 为获取邮箱验证码
        if (3 == type) {
            this.getCodeService = new PhoneCodeImpl();
        } else if (4 == type) {
            this.getCodeService = new EmailCodeImpl(sendEmailService);
        }
        return this;
    }

    public String getResult(String target) throws MessagingException {
        if (getCodeService == null) {
            return null;
        }
        String code = getCodeService.getCode(target);
        return code;
    }
}
