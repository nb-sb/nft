package com.nft.domain.support;

import com.nft.domain.user.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

public interface Token2User {
    UserVo getUserOne(HttpServletRequest http);
}
