package com.nft.domain.user.service.Factory.VerifyCode.verify;

import com.nft.common.Result;
import com.nft.domain.user.service.Factory.VerifyCode.mode.IVerifyMode;
import org.springframework.stereotype.Service;


public class EmailCodeVerify extends Verify{
    public EmailCodeVerify(IVerifyMode mode) {
        super(mode);
    }

    @Override
    public Result verifyCode(String inputCode, String targetKey) {
        Result verify = mode.verify(inputCode,targetKey);
        return verify;
    }
}
