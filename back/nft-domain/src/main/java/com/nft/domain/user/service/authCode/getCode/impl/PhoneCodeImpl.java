package com.nft.domain.user.service.authCode.getCode.impl;

import com.nft.common.Utils.CheckUtils;
import com.nft.domain.email.SendEmailService;
import com.nft.domain.user.service.authCode.getCode.IGetCodeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class PhoneCodeImpl implements IGetCodeService {


    @Override
    public String getCode(String target) {
        if (!CheckUtils.isMobile(target)) {
            return null;
        }
        return null;
    }
}
