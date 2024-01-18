package com.nft.domain.user.service.Factory.VerifyCode.verify;

import com.nft.common.Result;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.Factory.VerifyCode.mode.IVerifyMode;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OldPassWordVerify extends Verify{
//    @Autowired
    IUserInfoRepository iUserInfoRepository;
    public OldPassWordVerify(IVerifyMode mode) {
        super(mode);
    }

    @Override
    public Result verifyCode(String username, String oldPassword) {
        Result verify = mode.verify(username, oldPassword);
        return verify;
    }
    public Result verifyCode(String username,String newPassword,String oldPassword) {
        Result verify = mode.verify(username, oldPassword);
        return verify;
    }
}
