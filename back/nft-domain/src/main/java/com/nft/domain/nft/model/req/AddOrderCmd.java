package com.nft.domain.nft.model.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author: 戏人看戏
 * @Date: 2023/12/27 9:15
 * @Description: 抢购藏品请求信息
 */
@Data
public class AddOrderCmd {
    @NotNull
    @Min(value = 1, message = "商品id不能小于 0")
    private Integer collectionId; //藏品id
    @Null
    private Integer userId;
    @Null
    private String userAddress;
}
