package com.nft.domain.nft.model.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class TransferReq {
    @NotNull
    String toAddress; //转移的地址
    @NotNull
    @Min(1)
    Integer id; //所属藏品id
}
