package com.nft.trigger.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.app.user.UserService;
import com.nft.app.user.dto.CreatCmd;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.email.SendEmailService;
import com.nft.domain.support.Search;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.req.*;
import com.nft.domain.user.model.res.SelectRes;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.service.Info.IUserAccountService;
import com.nft.domain.user.service.Factory.authCode.AuthCodeFactory;
import com.nft.domain.user.service.Factory.authCode.getCode.IGetCodeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@Log4j2
@AllArgsConstructor
public class UserController {


    private final IUserAccountService iUserAccountService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final HttpServletRequest httpServletRequest;
    private final Token2User token2User;
    private final SendEmailService sendEmailService;
    private final AuthCodeFactory authCodeFactory;


    @GetMapping("/login")
    @ResponseBody
    public Result test(@Valid @RequestBody LoginReq loginReq) {
//        登录也需要判断验证码，否则防止被刷登录
//        UserResult res = getVerification(loginReq.getCodeId());
//        if (res != null) return res;
        return iUserAccountService.login(loginReq);
    }

    @GetMapping("/register")
    @ResponseBody
    public Result register(@Valid @RequestBody CreatCmd cmd) {
        //注册之前判断 验证码的id 如果不为true说明验证码验证过期或者没有验证成功
//        UserResult res = getVerification(signReq.getCodeId());
//        if (res != null) return res;
        return userService.creat(cmd);
    }

    //申请验证码--判断是手机验证还是邮箱验证生成相应的60秒验证码
    @PostMapping("/getcode")
    @ResponseBody
    public Result getcode(@Valid @RequestBody AuthCodeReq authCodeReq) {
        //获取验证码时需要进行滑动验证码验证等操作,防止脚本消息刷接口
//        Result res = getVerification(getCodeType.getCodeId());
//        if (res != null) return res;
        try {
            IGetCodeService codeService = authCodeFactory.getCodeService(authCodeReq.getType());
            if (codeService == null) {
                return new Result("0", "获取验证码类型错误");
            }
            String code = codeService.getCode(authCodeReq.getTarget());
            if (code != null) {
                //将验证码记录redis 5分钟
                return new Result("1", String.valueOf(redisUtil.set(authCodeReq.getTarget(),
                        code, Constants.RedisKey.MINUTE_5)));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Result("0", "获取验证码发送失败,请检查是否输入有误");
    }

    //修改密码
    @PostMapping("/chanagePassword")
    @ResponseBody
    public Result chanagePassword(@Valid @RequestBody ChanagePwReq chanagePwReq) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        return iUserAccountService.changePassword(userOne, chanagePwReq);
    }

    //    使用分页查询查询所有用户
    @GetMapping("/selectUserPage")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result selectUserPage(@Valid Search search) {
        //查询当前页码，查询条数
        List<UserVo> userVos = iUserAccountService.selectUserPage(
                new Page<>(search.getCurrent(), search.getPageSize()));
        return SelectRes.success(userVos);
    }

    //查询用户自己的个人信息
    @PostMapping("/getOwnerInfo")
    @ResponseBody
    public Result getOwnerInfo() {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        UserInfoVo userInfoVo = userService.selectUserAllInfo(userOne);
        return SelectRes.success(userInfoVo);
    }

    //提交实名认证信息
    @PostMapping("/submitRealNameAuth")
    @ResponseBody
    public Result submitRealNameAuth(@Valid @RequestBody RealNameAuthReq realNameAuthReq) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        return iUserAccountService.submitRealNameAuth(userOne, realNameAuthReq);
    }
    //todo 用户修改被驳回的认证信息并提交

    // TODO: 2023/12/29 管理员 审核实名认证信息
    public Result AuditRealNameAuth(@Valid @RequestBody UpdateRealNameAuthStatusReq req) {
        return iUserAccountService.AuditRealNameAuth(req);
    }
    //todo 修改用户信息

    //todo 添加头像等
    //验证验证码是否验证成功
    private Result getVerification(String codeId) {
//        2.判断验证码是否验证成功，如果没验证成功则返回验证失败
        Boolean o = (Boolean) Optional.ofNullable(redisUtil.get(codeId)).orElse(false);
        if (!o) {
            redisUtil.del(codeId);
            return Result.error("验证码验证失败");
        }
//        3.已经验证成功，删除这个key即可
        redisUtil.del(codeId);
        return null;
    }

}
