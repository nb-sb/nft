package com.nft.domain.user.service.Factory.VerifyCode.verify;

import com.nft.common.Result;
import com.nft.domain.user.service.Factory.VerifyCode.mode.IVerifyMode;
import org.springframework.stereotype.Service;

public class PhoneCodeVerify extends Verify {

    public PhoneCodeVerify(IVerifyMode mode) {
        super(mode);
    }

    @Override
    public Result verifyCode(String inputCode, String target) {
        Result verify = mode.verify(inputCode,target);
        return verify;
    }
}
