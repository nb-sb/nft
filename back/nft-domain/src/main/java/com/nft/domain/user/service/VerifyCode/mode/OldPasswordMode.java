package com.nft.domain.user.service.VerifyCode.mode;

import com.nft.common.Result;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.repository.IUserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@AllArgsConstructor
public class OldPasswordMode implements IVerifyMode {

    private final IUserInfoRepository iUserInfoRepository;

    @Override
    public Result verify(String username, String oldPassword) {
        UserEntity userVo;
        userVo = iUserInfoRepository.selectOne(username, oldPassword);
        if (userVo != null) {
            return Result.success("验证成功");
        }
        log.info("旧密码输入错误");
        return Result.error("旧密码输入错误");
    }
}
