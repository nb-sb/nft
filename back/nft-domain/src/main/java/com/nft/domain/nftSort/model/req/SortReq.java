package com.nft.domain.nftSort.model.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SortReq {

    @NotNull
    //藏品分类名称
    private String conllectionName;
    @NotNull
    //藏品分类代号
    private String conllectionSlug;

}
