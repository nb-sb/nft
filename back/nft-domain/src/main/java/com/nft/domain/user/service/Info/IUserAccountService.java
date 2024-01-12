package com.nft.domain.user.service.Info;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Result;
import com.nft.domain.user.model.req.*;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IUserAccountService {

//    登录服务
    UserResult login(LoginReq loginReq);

    //找回密码
    UserResult retrievePassword(LoginReq loginReq);

    //修改密码
    Result changePassword(UserVo fromUser, ChanagePwReq chanagePwReq);


    //使用分页查询用户信息
    List<UserVo> selectUserPage(Page<UserVo> page);

    Result submitRealNameAuth(UserVo fromUser,RealNameAuthReq realNameAuthReq);

    Result AuditRealNameAuth(UpdateRealNameAuthStatusReq req);
}
