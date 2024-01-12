package com.nft.domain.user.service.Factory.VerifyCode.Verify;

import com.nft.common.Result;
import com.nft.domain.user.model.req.ChanagePwReq;

public interface IVerifyService {

    Result Check(ChanagePwReq chanagePwReq);
}
