package com.nft.domain.user.service.VerifyCode;

import com.nft.common.Result;
import com.nft.domain.user.model.req.ChanagePwCmd;
import com.nft.domain.user.service.VerifyCode.mode.EmailCodeMode;
import com.nft.domain.user.service.VerifyCode.mode.IVerifyMode;
import com.nft.domain.user.service.VerifyCode.mode.OldPasswordMode;
import com.nft.domain.user.service.VerifyCode.mode.PhoneCodeMode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 戏人看戏
 * @Date: 2024/1/12 14:35
 * @Description: 使用策略模式用于不同验证类型
 */
@Service
public class VerifyFactory {

    private final Map<Integer, IVerifyMode> strategies;

    public VerifyFactory(OldPasswordMode oldPasswordMode, EmailCodeMode emailCodeMode, PhoneCodeMode phoneCodeMode) {
        strategies = new HashMap<>();
        strategies.put(1, oldPasswordMode);
        strategies.put(2, emailCodeMode);
        strategies.put(3, phoneCodeMode);
    }

    public Result getVerifyService(ChanagePwCmd chanagePwCmd) {
        IVerifyMode strategy = strategies.get(chanagePwCmd.getType());
        if (strategy != null) {
            return strategy.verify(chanagePwCmd.getInput_key(), chanagePwCmd.getTarget());
        }
        return null;
    }

}
