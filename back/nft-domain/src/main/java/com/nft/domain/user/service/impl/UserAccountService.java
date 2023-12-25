package com.nft.domain.user.service.impl;

import com.nft.common.Utils.CheckUtils;
import com.nft.common.Constants;
import com.nft.common.Utils.TokenUtils;
import com.nft.common.Redis.RedisUtil;
import com.nft.domain.user.model.req.*;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.IUserAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class UserAccountService implements IUserAccountService {

    @Autowired
    IUserInfoRepository iUserInfoRepository;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    TokenUtils tokenUtils;
    @Override
    public UserResult register(SignReq signReq) {
        if (!CheckUtils.isUserName(signReq.getUsername())) {
            return new UserResult("0", "用户名不合规(不包含特殊字符6-20位)");
        }
        if (!CheckUtils.isPassword(signReq.getPassword())) {
            return new UserResult("0", "密码不合规(6-16位)");
        }
        signReq.setRole(0);
        Constants.ResponseCode responseCode = iUserInfoRepository.register(signReq);

        if (responseCode.getCode() == Constants.ResponseCode.SUCCESS.getCode()) {
            return new UserResult("1", "注册成功");
        }
        return new UserResult("0", responseCode.getInfo());
    }

    @Override
    public UserResult login(LoginReq loginReq) {
        //判断账号密码是否正确
        UserVo res = iUserInfoRepository.selectOne(loginReq);
        Optional<UserVo> userVo1 = Optional.ofNullable(res);
        if (userVo1.isPresent()) {

            return new UserResult("1", "登录成功",tokenUtils.token(loginReq.getUsername(),loginReq.getPassword()));
        }
        return new UserResult("0", "账号或密码错误");
    }

    @Override
    public UserResult retrievePassword(LoginReq loginReq) {
        return null;
    }

    /**
     * @Des 修改密码逻辑
     * {
     *     //使用旧密码修改密码
     *     String username;
     *     String password;
     *     String oldpassword;
     *     String type; // 验证类型 2 是使用 使用验证码修改 , 1 是使用旧密码修改
     * }
     * {
     *     //使用邮箱或手机号密码验证需要传入的参数
     *     String username;
     *     String password;
     *     String phone;  OR  String email;
     *     String code; //验证码
     *     String type; // 验证类型 2 是使用 使用验证码修改 , 1 是使用旧密码修改
     * }
     */
    @Override
    public Object chanagePassword(ChanagePwReq chanagePwReq) {
//        判断逻辑 - 1.判断验证码是否正确 2.判断验证码是否合规
        Integer type = chanagePwReq.getType();
            //1.判断验证类型是邮箱验证还是旧密码修改
        if (Constants.Use_Verification_code_modify.equals(type)) {
            //2.如果是验证码验证的话则使用redis进行查询验证
            String code = Optional.ofNullable(chanagePwReq.getCode()).orElse("");
            String key = null;
            if (chanagePwReq.getEmail() !=null) {
                //如果是使用邮箱验证的话
                key = chanagePwReq.getEmail();
                if (!code.equals(redisUtil.get(key))) {
                    return "邮箱验证码不正确-验证失败";
                }
            } else {
                //使用手机号验证码验证
                key = chanagePwReq.getPhone();
                if (!code.equals(redisUtil.get(key))) {
                    return "手机号验证码不正确-验证失败";
                }
            }
            //到这里实际上判断验证码的逻辑已经结束,可以将redis中验证码进行删除
            redisUtil.del(key);
        }else {
            //使用旧密码修改
            if (chanagePwReq.getPassword().equals(chanagePwReq.getOldpassword())) {
                return "新旧密码一致!";
            }
            //判断旧密码是否正确
            LoginReq loginReq = new LoginReq();
            loginReq.setUsername(chanagePwReq.getUsername());
            loginReq.setPassword(chanagePwReq.getOldpassword());
            UserVo userVo = iUserInfoRepository.selectOne(loginReq);
            if (userVo == null) {
                return "旧密码不正确-验证失败";
            }
        }
        // 修改密码操作
        boolean b = iUserInfoRepository.chanagePassword(chanagePwReq);
        if (b) {
            return "修改成功!";
        }
        return "未知错误!";
    }

    @Override
    public List<UserVo> selectUserPage(Search search) {
        List<UserVo> o = iUserInfoRepository.selectUserPage(search);
        return o;
    }

    @Override
    public boolean isAdmin(String username,String password) {
        return false;
    }

    @Override
    public boolean isAdmin(HttpServletRequest http) {
        String token = http.getHeader("token");
        Optional<String> token1 = Optional.ofNullable(token);
        if (!token1.isPresent()) {
            log.warn("token 不存在");
            return false;
        }
        Map<String, String> userMap = tokenUtils.decodeToken(token1.get());

        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(userMap.get("username"))
                .setPassword(userMap.get("password"));
        UserVo userVo = iUserInfoRepository.selectOne(loginReq);
        if (Constants.ADMIN == userVo.getRole()) {
            return true;
        }
        log.warn("不是管理员权限"+userMap);
        return false;
    }
}
