package com.nft.domain.nft.model.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
* @author: 戏人看戏
* @Date: 2023/12/27 9:15
* @Description: 抢购藏品请求信息
*/
@Data
public class AddOrder {
    @NotNull
    @Min(value = 1,message = "商品id不能小于 0")
    private Integer id; //藏品id
}
