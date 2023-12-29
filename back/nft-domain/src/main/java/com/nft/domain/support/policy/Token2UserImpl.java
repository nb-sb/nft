package com.nft.domain.support.policy;

import com.nft.common.Utils.TokenUtils;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.req.LoginReq;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Service
public class Token2UserImpl implements Token2User {
    @Autowired
    IUserInfoRepository iUserInfoRepository;
    @Override
    public UserVo getUserOne(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Map<String, String> userMap = TokenUtils.decodeToken(token);
        if (userMap==null) return null;
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(userMap.get("username")).setPassword(userMap.get("password"));
        return iUserInfoRepository.selectOne(loginReq);
    }
}
