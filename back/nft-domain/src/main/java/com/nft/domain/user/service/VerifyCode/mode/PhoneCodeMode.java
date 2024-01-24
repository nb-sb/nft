package com.nft.domain.user.service.VerifyCode.mode;

import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PhoneCodeMode implements IVerifyMode {
    private final RedisUtil redisUtil;


    @Override
    public Result verify(String inputCode, String MobileNumber) {
        if (!inputCode.equals(redisUtil.get(MobileNumber))) {
            return Result.error("验证码验证失败");
        }
        redisUtil.del(MobileNumber);
        return Result.success("验证成功");
    }
}
