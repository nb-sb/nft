package com.nft.domain.user.service.VerifyCode.verify;

import com.nft.common.Result;
import com.nft.domain.user.service.VerifyCode.mode.IVerifyMode;

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
