package com.nft.domain.user.service.Factory.VerifyCode;

import com.nft.common.Result;
import com.nft.domain.user.model.req.ChanagePwReq;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.service.Factory.VerifyCode.mode.EmailCodeMode;
import com.nft.domain.user.service.Factory.VerifyCode.mode.OldPasswordMode;
import com.nft.domain.user.service.Factory.VerifyCode.mode.PhoneCodeMode;
import com.nft.domain.user.service.Factory.VerifyCode.verify.EmailCodeVerify;
import com.nft.domain.user.service.Factory.VerifyCode.verify.OldPassWordVerify;
import com.nft.domain.user.service.Factory.VerifyCode.verify.PhoneCodeVerify;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author: 戏人看戏
 * @Date: 2024/1/12 14:35
 * @Description: 使用桥接方式用于不同验证类型
 */
@Service
@AllArgsConstructor
public class VerifyFactory {
    private final OldPasswordMode oldPasswordMode;
    private final EmailCodeMode emailCodeMode;
    private final PhoneCodeMode phoneCodeMode;

    //1 是使用旧密码修改 2 是使用 使用邮箱验证码修改 , 3是使用手机验证码
    public Result getVerifyService(ChanagePwReq chanagePwReq) {
        if (chanagePwReq.getType().equals(1)) {
            //使用旧密码修改
            if (chanagePwReq.getPassword().equals(chanagePwReq.getOldpassword())) {
                return UserResult.error("修改前后密码相同!");
            }
            OldPassWordVerify oldPassWordVerify = new OldPassWordVerify(oldPasswordMode);
            return oldPassWordVerify.verifyCode(chanagePwReq.getUsername(), chanagePwReq.getOldpassword());
        } else if (chanagePwReq.getType().equals(2)) {
            EmailCodeVerify emailCodeVerify = new EmailCodeVerify(emailCodeMode);
            return emailCodeVerify.verifyCode(chanagePwReq.getCode(), chanagePwReq.getEmail());
        } else if (chanagePwReq.getType().equals(3)) {
            PhoneCodeVerify phoneCodeVerify = new PhoneCodeVerify(phoneCodeMode);
            return phoneCodeVerify.verifyCode(chanagePwReq.getPhone(), chanagePwReq.getPhone());
        }
        return null;
    }
}
