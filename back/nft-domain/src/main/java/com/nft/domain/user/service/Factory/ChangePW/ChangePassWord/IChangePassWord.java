package com.nft.domain.user.service.Factory.ChangePW.ChangePassWord;

import com.nft.common.Result;
import com.nft.domain.user.model.req.ChanagePwReq;

public interface IChangePassWord {

    Result Check(ChanagePwReq chanagePwReq);
}
