package com.nft.domain.user.model.vo;

import com.nft.domain.user.model.req.RealNameAuthReq;
import lombok.Data;

@Data
public class RealNameAuthVo extends RealNameAuthReq {
    Integer status;
}
