package com.nft.domain.user.service.Factory.ChangePW.ChangePassWord.impl;

import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import com.nft.domain.user.model.req.ChanagePwReq;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.service.Factory.ChangePW.ChangePassWord.IChangePassWord;
import sun.util.resources.cldr.nyn.CalendarData_nyn_UG;

import java.util.Optional;

public class EmailCode implements IChangePassWord {
    private RedisUtil redisUtil;
    public EmailCode(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public Result Check(ChanagePwReq chanagePwReq) {
        String key = chanagePwReq.getEmail();
        String code = chanagePwReq.getCode();
        if (code == null) {
            return UserResult.error("请输入邮箱验证码");
        }
        if (!chanagePwReq.getCode().equals(redisUtil.get(key))) {
            return UserResult.error("验证失败：邮箱验证码不正确！");
        }
        redisUtil.del(key);
        return null;
    }
}
