package com.nft.domain.support;


import com.nft.domain.user.model.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface Token2User {
    UserEntity getUserOne(HttpServletRequest http);
}
