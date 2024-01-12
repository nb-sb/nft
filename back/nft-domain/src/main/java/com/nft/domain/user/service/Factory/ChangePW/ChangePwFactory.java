package com.nft.domain.user.service.Factory.ChangePW;

import com.nft.common.Redis.RedisUtil;
import com.nft.domain.user.service.Factory.ChangePW.ChangePassWord.IChangePassWord;
import com.nft.domain.user.service.Factory.ChangePW.ChangePassWord.impl.EmailCode;
import com.nft.domain.user.service.Factory.ChangePW.ChangePassWord.impl.PhoneCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChangePwFactory {
    private final RedisUtil redisUtil;
    //1 是使用旧密码修改 2 是使用 使用邮箱验证码修改 , 3是使用手机验证码
    public IChangePassWord getChangePwService(Integer type) {
        if (type.equals(2)) {
            return new EmailCode(redisUtil);
        } else if (type.equals(3)) {
            return new PhoneCode(redisUtil);
        }
        return null;
    }
}
