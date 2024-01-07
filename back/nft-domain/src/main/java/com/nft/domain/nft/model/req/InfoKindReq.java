package com.nft.domain.nft.model.req;

import com.nft.common.PageRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class InfoKindReq extends PageRequest {
    /**
     * 分类id
     */
    @NotNull
    @Min(value = 1,message = "mid 最小值为1")
    private int min = 1;
}
