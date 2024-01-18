package com.nft.domain.user.service.Factory.authCode;

import com.nft.domain.user.model.entity.AuthCodeActionEntity;
import com.nft.domain.user.service.Factory.authCode.getCode.IGetCodeService;
import com.nft.domain.user.service.Factory.authCode.getCode.impl.EmailCodeImpl;
import com.nft.domain.user.service.Factory.authCode.getCode.impl.PhoneCodeImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Map;

/**
* @author: 戏人看戏
* @Date: 2024/1/12 14:35
* @Description: 策略模式
*/
@Service
public class AuthCodeService {

    @Resource
    private DefaultAuthCodeFactory authCodeFactory;
    private IGetCodeService getCodeService;

    public AuthCodeService AuthCodeService(Integer type) {
        Map<String, IGetCodeService<AuthCodeActionEntity.AuthEntity>> stringIGetCodeServiceMap = authCodeFactory.openLogicAuth();
        //3 为获取手机验证码 4 为获取邮箱验证码
        if (3 == type) {
            IGetCodeService<AuthCodeActionEntity.AuthEntity> authEntityIGetCodeService = stringIGetCodeServiceMap.get(DefaultAuthCodeFactory.AuthCodeModel.PHONE.getType());
            this.getCodeService = authEntityIGetCodeService;
        } else if (4 == type) {
            //获取邮箱
            IGetCodeService<AuthCodeActionEntity.AuthEntity> authEntityIGetCodeService = stringIGetCodeServiceMap.get(DefaultAuthCodeFactory.AuthCodeModel.EMAIL.getType());
            this.getCodeService = authEntityIGetCodeService;
        }
        return this;
    }

    public String getResult(String target) throws MessagingException {
        if (getCodeService == null) {
            return null;
        }
//        EmailCodeImpl emailCode = new EmailCodeImpl();
//        AuthCodeActionEntity<AuthCodeActionEntity.AuthEmailEntity> code = emailCode.getCode(target);
        AuthCodeActionEntity<AuthCodeActionEntity.AuthEmailEntity> code1 = getCodeService.getCode(target);
        return code1.getData().getRes();
    }
}
