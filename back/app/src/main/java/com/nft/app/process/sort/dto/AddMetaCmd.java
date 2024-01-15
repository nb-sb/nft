package com.nft.app.process.sort.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddMetaCmd {
    @NotNull
    private String name;//藏品分类名称
    @NotNull
    private String slug;//藏品分类代号
}
