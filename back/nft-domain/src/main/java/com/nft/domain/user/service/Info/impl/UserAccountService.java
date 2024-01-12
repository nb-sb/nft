package com.nft.domain.user.service.Info.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Redis.RedisConstant;
import com.nft.common.Result;
import com.nft.common.Constants;
import com.nft.common.Utils.TokenUtils;
import com.nft.common.Redis.RedisUtil;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.req.*;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.RealNameAuthVo;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserDetalRepository;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.Factory.ChangePW.ChangePassWord.IChangePassWord;
import com.nft.domain.user.service.Factory.ChangePW.ChangePwFactory;
import com.nft.domain.user.service.Info.IUserAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    ChangePwFactory changePwFactory;


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
    public Result changePassword(UserVo fromUser, ChanagePwReq chanagePwReq) {

//        判断逻辑 - 1.判断验证码是否正确 2.判断验证码是否合规
        Integer type = chanagePwReq.getType();
        //1.判断验证类型
        if (!Constants.Use_OrderPassword_modification.equals(type)) {
            IChangePassWord changePwService = changePwFactory.getChangePwService(chanagePwReq.getType());
            Result check = changePwService.Check(chanagePwReq);
            if (check != null) {
                return check;
            }
        }else {
            //使用旧密码修改
            if (chanagePwReq.getPassword().equals(chanagePwReq.getOldpassword())) {
                return UserResult.error("修改前后密码相等!");
            }
            //判断旧密码是否正确
            if (fromUser == null) {
                return UserResult.error("需要登录后才能使用旧密码修改");
            }
            LoginReq loginReq = new LoginReq();
            loginReq.setUsername(fromUser.getUsername());
            chanagePwReq.setUsername(fromUser.getUsername());
            loginReq.setPassword(chanagePwReq.getOldpassword());
            UserVo userVo = iUserInfoRepository.selectOne(loginReq);
            if (userVo == null) {
                log.info("token 验证失败，没找到此用户");
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
