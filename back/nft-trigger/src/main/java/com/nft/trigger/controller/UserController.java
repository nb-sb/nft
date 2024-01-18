package com.nft.trigger.controller;

import com.nft.app.process.user.UserCommandService;
import com.nft.app.process.user.UserQueryService;
import com.nft.app.process.user.dto.CreatCmd;
import com.nft.app.process.user.dto.LoginCmd;
import com.nft.common.Constants;
import com.nft.common.PageRequest;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.model.req.AuthCodeReq;
import com.nft.domain.user.model.req.ChanagePwCmd;
import com.nft.domain.user.model.req.RealNameAuthCmd;
import com.nft.domain.user.model.req.UpdateRealNameAuthStatusCmd;
import com.nft.domain.user.model.res.SelectRes;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.service.authCode.AuthCodeService;
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


    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final RedisUtil redisUtil;
    private final HttpServletRequest httpServletRequest;
    private final Token2User token2User;
    private final AuthCodeService authCodeService;


    @GetMapping("/login")
    @ResponseBody
    public Result test(@Valid @RequestBody LoginCmd loginCmd) {
//        登录也需要判断验证码，否则防止被刷登录
//        UserResult res = getVerification(loginReq.getCodeId());
//        if (res != null) return res;
        return userQueryService.login(loginCmd);
    }

    @GetMapping("/register")
    @ResponseBody
    public Result register(@Valid @RequestBody CreatCmd cmd) {
        //注册之前判断 验证码的id 如果不为true说明验证码验证过期或者没有验证成功
//        UserResult res = getVerification(signReq.getCodeId());
//        if (res != null) return res;
        userCommandService.creat(cmd);
        return null;
    }

    //申请验证码--判断是手机验证还是邮箱验证生成相应的60秒验证码
    @PostMapping("/getcode")
    @ResponseBody
    public Result getcode(@Valid @RequestBody AuthCodeReq authCodeReq) {
        //获取验证码时需要进行滑动验证码验证等操作,防止脚本消息刷接口
//        Result res = getVerification(getCodeType.getCodeId());
//        if (res != null) return res;
        try {
            String code = authCodeService.AuthCodeService(authCodeReq.getType())
                    .getResult(authCodeReq.getTarget());
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
    public Result chanagePassword(@Valid @RequestBody ChanagePwCmd cmd) {
        UserEntity userOne = token2User.getUserOne(httpServletRequest);
        cmd.setPassword(userOne.getUsername());
        return userCommandService.changePassword(cmd);
    }

    //    使用分页查询查询所有用户
    @GetMapping("/selectUserPage")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result selectUserPage(@Valid PageRequest pageRequest) {
        //查询当前页码，查询条数
//        List<UserVo> userVos = iUserAccountService.selectUserPage(
//                new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize()));
        List<UserEntity> userVos = userQueryService.pageList(pageRequest);
        return SelectRes.success(userVos);
    }

    //查询用户自己的个人信息
    @PostMapping("/getOwnerInfo")
    @ResponseBody
    public Result getOwnerInfo() {
        UserEntity userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        UserInfoVo userInfoVo = userQueryService.selectUserInfo(userOne);
        return SelectRes.success(userInfoVo);
    }

    //提交实名认证信息
    @PostMapping("/submitRealNameAuth")
    @ResponseBody
    public Result submitRealNameAuth(@Valid @RequestBody RealNameAuthCmd cmd) {
        UserEntity userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        cmd.setAddress(userOne.getAddress());
        cmd.setForId(userOne.getId());
        return userCommandService.RealNameAuth(cmd);
    }
    //todo 用户修改被驳回的认证信息并提交

    // TODO: 2023/12/29 管理员 审核实名认证信息
    public Result AuditRealNameAuth(@Valid @RequestBody UpdateRealNameAuthStatusCmd cmd) {
        return userCommandService.AuditRealNameAuth(cmd);
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
