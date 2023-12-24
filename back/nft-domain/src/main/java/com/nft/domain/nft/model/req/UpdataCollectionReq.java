package com.nft.domain.nft.model.req;

import com.nft.domain.common.anno.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdataCollectionReq {
    @NotNull
    private Integer id;//出售表中藏品id
    private String name;//藏品名称
    private String present; //介绍
    @Status(statusType = {"0","1"})
    private Integer status;//出售状态 1为正常 0为闭售
}
