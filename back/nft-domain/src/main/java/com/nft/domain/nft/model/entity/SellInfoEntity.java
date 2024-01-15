package com.nft.domain.nft.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import sun.reflect.generics.tree.VoidDescriptor;

@Data
@Accessors(chain = true)
public class SellInfoEntity {
    private Integer id;

    private Integer uniqueId;

    // @ApiModelProperty(value = "藏品hash")
    private String hash;

    // @ApiModelProperty(value = "发行量")
    private Integer amount;

    // @ApiModelProperty(value = "剩余数量")
    private Integer remain;

    // @ApiModelProperty(value = "数字藏品作者地址")
    private String auther;

    // @ApiModelProperty(value = "# 1 为正常 ，  0 为闭售")
    private Integer status;

    private String ipfsHash;
    public void init() {
        this.status = 1;
        this.remain = this.amount;
    }

}
