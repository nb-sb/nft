package com.nft.domain.nft.model.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
public class SellReq {
    @NotNull
    private String path; //文件路径
    @NotNull
    @Min(1)
    private Integer total; //总数
    @NotNull
    private String present; //介绍
    @NotNull
    private String name; //藏品名称
    @NotNull
    private BigDecimal price; //价格

    private String hash; //hash
    @NotNull
    private Integer mid ; //分类id

}
