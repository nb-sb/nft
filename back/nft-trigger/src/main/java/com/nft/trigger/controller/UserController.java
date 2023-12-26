package com.nft.trigger.controller;

import com.nft.common.Result;
import com.nft.common.Utils.CheckUtils;
import com.nft.common.Constants;
import com.nft.common.SendEmail;
import com.nft.common.Redis.RedisUtil;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.user.model.req.*;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.service.IUserAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@AllArgsConstructor
public class UserController {


    private final IUserAccountService iUserAccountService;
    private final RedisUtil redisUtil;
    private final SendEmail sendEmail;



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
    public Result register(@Valid @RequestBody SignReq signReq) {
        //注册之前判断 验证码的id 如果不为true说明验证码验证过期或者没有验证成功
        UserResult res = getVerification(signReq.getCodeId());
        if (res != null) return res;
        return iUserAccountService.register(signReq);
    }
    //申请验证码--判断是手机验证还是邮箱验证生成相应的60秒验证码
    @PostMapping("/getcode")
    @ResponseBody
    public Result getcode(@Valid @RequestBody GetCodeType getCodeType)  {
        //获取验证码时需要进行滑动验证码验证等操作,防止脚本消息刷接口
        UserResult res = getVerification(getCodeType.getCodeId());
        if (res != null) return res;
        String name = getCodeType.getName();
        if (Constants.Get_Code_iphone.equals(getCodeType.getType())) {
            //如果是手机号类型 验证手机是否正确
            if (!CheckUtils.isMobile(name)) {
                return new Result("0","手机号错误");
            }
            //1.调用发送验证码方法
            int code = (int) (Math.random() * 50000);//模拟获取验证码操作
            //todo: 调用发送逻辑
            //2.记录到redis中设置60秒 key: name value: code
            //3.返回成功或失败消息
            return new Result("0", String.valueOf(redisUtil.set(name, code, 60)));
        } else if(Constants.Get_Code_email.equals(getCodeType.getType())) {
            //如果是邮箱类型 验证邮箱类型
            if (!CheckUtils.isEmail(name)) {
                return new Result("0","邮箱类型错误");
            }
            //1.调用发送验证码方法
            int code = (int) (Math.random() * 50000);//模拟获取验证码操作
            try {
                sendEmail.sentSimpleMail("您的NFT修改密码的验证码", "您的NFT修改密码的验证码为: " + code, name);
            } catch (MessagingException e) {
                log.error("邮箱发送验证码时出错 :"+e);
                return new Result("0","未知错误-请联系管理员查看日志");
            }
            //2.记录到redis中设置60秒 key: name value: code
            //3.返回成功或失败消息
            return new Result("0", String.valueOf(redisUtil.set(name, code, 60)));
        } else {
            log.error("类型错误!"+getCodeType.getType());
            return new Result("0","获取验证码类型错误");
        }

    }

    //修改密码 -- 1.待优化返回接口2.应使用token拿到用户名
    @PostMapping("/chanagePassword")
    @ResponseBody
    public Object chanagePassword(@Valid @RequestBody ChanagePwReq chanagePwReq) {
        Object o = iUserAccountService.chanagePassword(chanagePwReq);
        return o;
    }

    //    使用分页查询查询所有用户
    @PostMapping("/selectUserPage")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public List<UserVo> selectUserPage(@RequestBody Search search) {
        return iUserAccountService.selectUserPage(search);
    }

    //todo 修改用户信息

    //验证验证码是否验证成功
    private UserResult getVerification(String codeId) {
//        2.判断验证码是否验证成功，如果没验证成功则返回验证失败
        Boolean o = (Boolean) Optional.ofNullable(redisUtil.get(codeId)).orElse(false);
        if (!o) {
            redisUtil.del(codeId);
            return new UserResult("0", "验证码验证失败");
        }
//        3.已经验证成功，删除这个key即可
        redisUtil.del(codeId);
        return null;
    }
}
