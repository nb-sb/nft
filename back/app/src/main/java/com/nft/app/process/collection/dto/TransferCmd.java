package com.nft.app.process.collection.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class TransferCmd {
    @NotNull
    private String toAddress; //转移的地址
    @NotNull
    @Min(1)
    private Integer id; //所属藏品id
    @Null
    private String userPrivatekey;
    @Null
    private String userAddress;
}
