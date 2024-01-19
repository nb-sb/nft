package com.nft.domain.user.service.VerifyCode.verify;

import com.nft.common.Result;
import com.nft.domain.user.service.VerifyCode.mode.IVerifyMode;

public abstract class Verify {
    protected IVerifyMode mode;
    public Verify(IVerifyMode mode) {
        this.mode = mode;
    }
    public abstract Result verifyCode(String inputCode, String targetKey);
}
