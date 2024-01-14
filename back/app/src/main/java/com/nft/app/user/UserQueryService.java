package com.nft.app.user;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.app.user.dto.LoginCmd;
import com.nft.common.Constants;
import com.nft.common.PageRequest;
import com.nft.common.Redis.RedisConstant;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.common.Utils.TokenUtils;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.Factory.UserEntityFatory;
import com.nft.infrastructure.po.UserInfo;
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
    public UserInfoVo selectUserAllInfo(UserVo userOne) {
        //查询用户个人信息。由于个人信息基本是不变的所以可以直接存入redis中
        String key = Constants.RedisKey.USER_INFO(userOne.getId());
        UserInfoVo userDetailByRedis = getUserDetailByRedis(key);
        if (userDetailByRedis != null) {
            return userDetailByRedis;
        }
        UserInfoVo userInfoVo = iUserInfoRepository.selectUserDetail(userOne.getId());
        if (userInfoVo == null) {
            return null;
        }
        userInfoVo.setUsername(userOne.getUsername());
        userInfoVo.setPassword("******************");
        userInfoVo.setRole(userOne.getRole());
        userInfoVo.setBalance(userOne.getBalance());
        redisUtil.set(key, JSONUtil.toJsonStr(userInfoVo), RedisConstant.DAY_ONE);
        return userInfoVo;
    }
    private UserInfoVo getUserDetailByRedis(String key) {
        String userInfoStr = redisUtil.getStr(key);
        UserInfoVo bean = JSONUtil.toBean(userInfoStr, UserInfoVo.class);
        return bean;
    }
}
