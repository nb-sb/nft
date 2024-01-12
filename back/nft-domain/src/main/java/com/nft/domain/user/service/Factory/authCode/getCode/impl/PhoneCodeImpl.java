package com.nft.domain.user.service.Factory.authCode.getCode.impl;

import com.nft.common.Utils.CheckUtils;
import com.nft.domain.user.service.Factory.authCode.getCode.IGetCodeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
public class PhoneCodeImpl implements IGetCodeService {

    @Override
    public String getCode(String target) {
        if (!CheckUtils.isMobile(target)) {
            return null;
        }
        return null;
    }
}
