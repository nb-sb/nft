package com.nft.app.user.dto;

import com.nft.domain.user.model.req.LoginReq;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 创建用户请求
 */
@Data
@Accessors(chain = true)
public class CreatCmd extends LoginReq {
    Integer role;
}
