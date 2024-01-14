package com.nft.domain.user.model.vo;

import com.nft.domain.user.model.req.RealNameAuthCmd;
import lombok.Data;

@Data
public class RealNameAuthVo extends RealNameAuthCmd {
    Integer status;
}
