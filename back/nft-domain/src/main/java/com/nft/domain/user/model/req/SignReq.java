package com.nft.domain.user.model.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignReq extends LoginReq{
    Integer role;
}
