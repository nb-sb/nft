package com.nft.domain.user.service.Factory.VerifyCode;

import com.nft.common.Redis.RedisUtil;
import com.nft.domain.user.service.Factory.VerifyCode.Verify.IVerifyService;
import com.nft.domain.user.service.Factory.VerifyCode.Verify.impl.EmailCode;
import com.nft.domain.user.service.Factory.VerifyCode.Verify.impl.PhoneCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
/**
* @author: 戏人看戏
* @Date: 2024/1/12 14:35
* @Description: 一个简单的验证验证码的工厂类
*/
@Service
@AllArgsConstructor
public class VerifyFactory {
    private final RedisUtil redisUtil;
    //1 是使用旧密码修改 2 是使用 使用邮箱验证码修改 , 3是使用手机验证码
    public IVerifyService getVerifyService(Integer type) {
        if (type.equals(2)) {
            return new EmailCode(redisUtil);
        } else if (type.equals(3)) {
            return new PhoneCode(redisUtil);
        }
        return null;
    }
}
