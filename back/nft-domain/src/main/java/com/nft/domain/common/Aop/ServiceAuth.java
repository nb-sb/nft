package com.nft.domain.common.Aop;


import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.common.Utils.TokenUtils;
import com.nft.domain.user.model.req.LoginReq;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


//交给spring管理
@Component
//标识切面
@Aspect
@Slf4j
public class ServiceAuth {
    private  IUserInfoRepository iUserInfoRepository;
    @Autowired
    public ServiceAuth( IUserInfoRepository iUserInfoRepository){
        this.iUserInfoRepository = iUserInfoRepository;
    }

    // 以自定义注解为切点
    @Pointcut("@annotation(com.nft.domain.common.Aop.AuthPermisson)")
    public void AuthPermisson(){}

    @Around("AuthPermisson()")
    public Object around(ProceedingJoinPoint pjp ) throws Throwable {
//        log.info("执行前1111");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        AuthPermisson annotation = method.getAnnotation(AuthPermisson.class);
        Integer value = annotation.value();
        //获取注解参数
//        System.out.println(value);
        Map<String, Object> map = null;
        //验证token的逻辑
        if (value.equals(Constants.permiss.everyone)) {
            map = isExist(token);
        } else if (value.equals(Constants.permiss.admin)) {
            map = IsAdmin(token);
        } else if (value.equals(Constants.permiss.regularUser)) {
            map = IsUser(token);
        }

        if ("401".equals(map.get("code"))) {
            return Result.buildResult("401", (String) map.get("data"));
        } else {
            return pjp.proceed();
        }
    }
    private Map<String,Object> IsAdmin(String token) {
        // System.out.println(token);
        HashMap<String, Object> map = new HashMap<>();
        if (token == null) {
            map.put("code", "401");
            map.put("data", "未登录");
            return map;
        }
        if (!TokenUtils.verify(token)) {
            map.put("code", "401");
            map.put("data", "身份验证失败");
            return map;
        }
        Map<String, String> stringStringMap = TokenUtils.decodeToken(token);
        String username = stringStringMap.get("username");
        String password = stringStringMap.get("password");
        UserVo userVo = iUserInfoRepository.selectOne(new LoginReq().setUsername(username).setPassword(password));
        // System.out.println(userPo1.getRole());
        if (userVo.getRole() != 1) {
            map.put("code", "401");
            map.put("data", "身份验证失败，只有管理员才可以调用此功能");
            return map;
        }
        map.put("code", "1");
        map.put("data", "成功");
        return map;
    }
    private Map<String,Object> IsUser(String token) {
        // System.out.println(token);
        HashMap<String, Object> map = new HashMap<>();
        if (token == null) {
            map.put("code", "401");
            map.put("data", "未登录");
            return map;
        }
        if (!TokenUtils.verify(token)) {
            map.put("code", "401");
            map.put("data", "身份验证失败");
            return map;
        }
        Map<String, String> stringStringMap = TokenUtils.decodeToken(token);
        String username = stringStringMap.get("username");
        String password = stringStringMap.get("password");
        UserVo userVo = iUserInfoRepository.selectOne(new LoginReq().setUsername(username).setPassword(password));
        // System.out.println(userPo1.getRole());
        if (userVo.getRole() != 0) {
            map.put("code", "401");
            map.put("data", "身份验证失败，只有普通用户才有权限调用");
            return map;
        }
        map.put("code", "1");
        map.put("data", "成功");
        return map;
    }
    private Map<String,Object> isExist (String token) {
        // System.out.println(token);
        HashMap<String, Object> map = new HashMap<>();
        if (token == null) {
            map.put("code", "401");
            map.put("data", "未登录");
            return map;
        }
        if (!TokenUtils.verify(token)) {
            map.put("code", "401");
            map.put("data", "身份验证失败");
            return map;
        }
        Map<String, String> stringStringMap = TokenUtils.decodeToken(token);
        String username = stringStringMap.get("username");
        String password = stringStringMap.get("password");
        UserVo userVo = iUserInfoRepository.selectOne(new LoginReq().setUsername(username).setPassword(password));
        // System.out.println(userPo1.getRole());
        if (userVo== null) {
            map.put("code", "401");
            map.put("data", "身份验证失败，用户不存在");
            return map;
        }
        map.put("code", "1");
        map.put("data", "成功");
        return map;
    }
}

