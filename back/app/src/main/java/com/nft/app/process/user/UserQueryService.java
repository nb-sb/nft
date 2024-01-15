package com.nft.app.process.user;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.app.process.user.dto.LoginCmd;
import com.nft.common.Constants;
import com.nft.common.PageRequest;
import com.nft.common.Redis.RedisConstant;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.common.Utils.TokenUtils;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.UserDetalVo;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.Factory.UserEntityFatory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserQueryService {
    private final IUserInfoRepository iUserInfoRepository;
    private final RedisUtil redisUtil;
    private final UserEntityFatory userEntityFatory;
    public Result login(LoginCmd cmd) {
        //判断账号密码是否正确
        UserVo res = iUserInfoRepository.selectOne(cmd.getUsername(),cmd.getPassword());
        Optional<UserVo> userVo1 = Optional.ofNullable(res);
        if (userVo1.isPresent()) {
            return UserResult.success("登录成功", TokenUtils.token(cmd.getUsername(), cmd.getPassword()));
        }
        return UserResult.error("账号或密码错误");
    }
    public List<UserVo> pageList(PageRequest pageRequest) {

        List<UserEntity> userEntities = iUserInfoRepository.selectUserPage(new Page<>(pageRequest.getCurrent(),
                pageRequest.getPageSize()));
        List<UserVo> userVos = BeanCopyUtils.convertListTo(userEntities, UserVo::new);
        return userVos;
    }
    public UserInfoVo selectUserInfo(UserVo userOne) {
        //查询用户个人信息。由于个人信息基本是不变的所以可以直接存入redis中
        String key = Constants.RedisKey.USER_INFO(userOne.getId());
        UserInfoVo userDetailByRedis = getUserDetailByRedis(key);
        if (userDetailByRedis != null) {
            return userDetailByRedis;
        }
        UserDetalVo userDetalVo = iUserInfoRepository.selectOneByForId(userOne.getId());
        if (userDetalVo == null) {
            return null;
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setAddress(userDetalVo.getAddress());
        userInfoVo.setPrivatekey(userOne.getPrivatekey());
        userInfoVo.setCardId(userDetalVo.getCardid());
        userInfoVo.setPhoneNumber(userDetalVo.getPhoneNumber());
        userInfoVo.setUsername(userOne.getUsername());
        userInfoVo.desensitisationPassword();
        userInfoVo.setBalance(userOne.getBalance());
        redisUtil.set(key, JSONUtil.toJsonStr(userInfoVo), RedisConstant.DAY_ONE);
        return userInfoVo;
    }
    private UserInfoVo getUserDetailByRedis(String key) {
        String userInfoStr = redisUtil.getStr(key);
        if (userInfoStr == null) {
            return null;
        }
        UserInfoVo bean = JSONUtil.toBean(userInfoStr, UserInfoVo.class);
        return bean;
    }
}
