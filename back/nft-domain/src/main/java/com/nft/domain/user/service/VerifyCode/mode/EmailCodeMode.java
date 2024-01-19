package com.nft.domain.user.service.VerifyCode.mode;

import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailCodeMode implements IVerifyMode{
    private final RedisUtil redisUtil;
    @Override
    public Result verify(String inputCode,String target) {
        if (!inputCode.equals(redisUtil.get(target))) {
            return Result.error("验证码验证失败");
        }
        redisUtil.del(target);
        return Result.success("验证成功");
    }
}
