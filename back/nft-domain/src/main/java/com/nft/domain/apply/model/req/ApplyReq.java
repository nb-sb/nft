package com.nft.domain.apply.model.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
public class ApplyReq {
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
    @NotNull
    private String hash; //hash 上传文件获得的文件hash
    @NotNull
    private Integer mid ; //分类id

}
