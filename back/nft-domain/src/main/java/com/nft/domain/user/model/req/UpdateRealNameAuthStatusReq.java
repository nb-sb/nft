package com.nft.domain.user.model.req;

import com.nft.domain.common.anno.Status;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateRealNameAuthStatusReq {
    @NotNull
    @Min(1)
    Integer id;
    @NotNull
    @Status(statusType = {"1","2"})
    Integer status;
}
