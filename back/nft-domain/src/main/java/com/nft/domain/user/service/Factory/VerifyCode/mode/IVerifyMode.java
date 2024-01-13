package com.nft.domain.user.service.Factory.VerifyCode.mode;

import com.nft.common.Result;

public interface IVerifyMode {
    Result verify(String inputCode, String target);
}
