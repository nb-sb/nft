package com.nft.app.user.dto;

import com.nft.domain.user.model.req.LoginReq;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatCmd extends LoginReq {
    Integer role;
}
