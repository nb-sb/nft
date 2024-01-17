package com.nft.domain.apply.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SubmitSellEntity {
    private Integer id;

    // @ApiModelProperty(value = "文件路径")
    private String path;

    // @ApiModelProperty(value = "出售数量")
    private Integer total;

    // @ApiModelProperty(value = "介绍")
    private String present;

    // @ApiModelProperty(value = "藏品姓名")
    private String name;

    // @ApiModelProperty(value = "作者 所属id")
    private String authorId;

    // @ApiModelProperty(value = "作者 区块链账户地址")
    private String authorAddress;

    // @ApiModelProperty(value = "售价")
    private BigDecimal price;

    // @ApiModelProperty(value = "审核状态 | 0为未审核 ，1 为审核不通过，2为审核成功")
    private Integer status;

    private String hash;
    public void init() {
        this.status = 0;
    }

}
