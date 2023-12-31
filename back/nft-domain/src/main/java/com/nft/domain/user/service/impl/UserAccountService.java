package com.nft.domain.user.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Result;
import com.nft.common.Utils.CheckUtils;
import com.nft.common.Constants;
import com.nft.common.Utils.TokenUtils;
import com.nft.common.Redis.RedisUtil;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.support.Search;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.req.*;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.RealNameAuthVo;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserDetalRepository;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.IUserAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class UserAccountService implements IUserAccountService {

    @Autowired
    IUserInfoRepository iUserInfoRepository;
    @Autowired
    IUserDetalRepository iUserDetalRepository;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    Token2User token2User;
    @Override
    public UserResult register(SignReq signReq) {
        if (!CheckUtils.isUserName(signReq.getUsername())) {
            return UserResult.error("用户名不合规(不包含特殊字符6-20位)");
        }
        if (!CheckUtils.isPassword(signReq.getPassword())) {
            return UserResult.error("密码不合规(6-16位)");
        }
        signReq.setRole(0);
        Constants.ResponseCode responseCode = iUserInfoRepository.register(signReq);

        if (Objects.equals(responseCode.getCode(), Constants.ResponseCode.SUCCESS.getCode())) {
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
            return UserResult.success("登录成功", TokenUtils.token(loginReq.getUsername(), loginReq.getPassword()));
        }

        return UserResult.error("账号或密码错误");
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
    public Result chanagePassword(UserVo fromUser,ChanagePwReq chanagePwReq) {

//        判断逻辑 - 1.判断验证码是否正确 2.判断验证码是否合规
        Integer type = chanagePwReq.getType();
            //1.判断验证类型是邮箱验证还是旧密码修改
        if (Constants.Use_Verification_code_modify.equals(type)) {
            //2.如果是验证码验证的话则使用redis进行查询验证
            String code = Optional.ofNullable(chanagePwReq.getCode()).orElse("");
            String key;
            if (chanagePwReq.getEmail() !=null) {
                //如果是使用邮箱验证的话
                key = chanagePwReq.getEmail();
                if (!code.equals(redisUtil.get(key))) {
                    return UserResult.error("邮箱验证码不正确-验证失败");
                }
            } else {
                //使用手机号验证码验证
                key = chanagePwReq.getPhone();
                if (!code.equals(redisUtil.get(key))) {
                    return UserResult.error("手机号验证码不正确-验证失败");
                }
            }
            //到这里实际上判断验证码的逻辑已经结束,可以将redis中验证码进行删除
            redisUtil.del(key);
        }else {
            //使用旧密码修改
            if (chanagePwReq.getPassword().equals(chanagePwReq.getOldpassword())) {
                return UserResult.error("不能使用近期使用过的密码!");
            }
            //判断旧密码是否正确
            LoginReq loginReq = new LoginReq();
            loginReq.setUsername(fromUser.getUsername());
            chanagePwReq.setUsername(fromUser.getUsername());
            loginReq.setPassword(chanagePwReq.getOldpassword());
            UserVo userVo = iUserInfoRepository.selectOne(loginReq);
            if (userVo == null) {
                return UserResult.error("旧密码不正确-验证失败");
            }
        }
        // 修改密码操作
        boolean b = iUserInfoRepository.chanagePassword(chanagePwReq);
        if (b) {
            return Result.success("修改成功!");
        }
        return UserResult.error("未知错误");
    }

    @Override
    public List<UserVo> selectUserPage(Page page) {
        return iUserInfoRepository.selectUserPage(page);
    }

    @Override
    public boolean isAdmin(String username,String password) {
        return false;
    }

    @Override
    public boolean isAdmin(HttpServletRequest http) {
        UserVo userVo = token2User.getUserOne(http);
        if (userVo == null) {
            log.error("token错误:"+userVo);
            return false;
        }
        if (Constants.ADMIN == userVo.getRole()) {
            return true;
        }
        log.warn("不是管理员权限: "+userVo);
        return false;
    }

    @Override
    public UserInfoVo selectUserDetail(UserVo userOne) {
        //查询用户个人信息。由于个人信息基本是不变的所以可以直接存入redis中
        UserInfoVo userDetailByRedis = getUserDetailByRedis(Constants.RedisKey.USER_INFO(userOne.getId()));
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
        redisUtil.set(Constants.RedisKey.USER_INFO(userOne.getId()), JSONUtil.toJsonStr(userInfoVo),60*30);
        return userInfoVo;
    }

    @Override
    public Result submitRealNameAuth(UserVo fromUser,RealNameAuthReq realNameAuthReq) {
        realNameAuthReq.setAddress(fromUser.getAddress());
        realNameAuthReq.setForid(fromUser.getId());
        //判断自己是否已经存在了认证信息，存在则无需提交
        RealNameAuthVo realNameAuthVo = iUserDetalRepository.selectByForId(fromUser.getId());
        if (realNameAuthVo != null) {
            return Result.error("有待审核的认证，请等待审核！");
        }
        if (iUserDetalRepository.submitRealNameAuth(realNameAuthReq))return new Result("1","提交成功");
        return Result.error("提交失败");
    }

    @Override
    public Result AuditRealNameAuth(UpdateRealNameAuthStatusReq req) {
        RealNameAuthVo realNameAuthReq = iUserDetalRepository.selectById(req.getId());
        if (realNameAuthReq == null) {
            //返回 审核的id不存在
            return Result.error("审核的id不存在");
        }
        //判断状态需要为待审核才能进行修改
        if (!Constants.realNameAuthStatus.AWAIT_AUDIT.equals(realNameAuthReq.getStatus())) {
            //返回 该内容已经被审核
            return Result.error("该信息已经被审核");
        }
        boolean b = iUserDetalRepository.updataStatusById(req);
        return Result.success("成功");
    }

    private UserInfoVo getUserDetailByRedis(String key) {
        String userInfoStr = redisUtil.getStr(key);
        UserInfoVo bean = JSONUtil.toBean(userInfoStr, UserInfoVo.class);
        return bean;
    }

}
