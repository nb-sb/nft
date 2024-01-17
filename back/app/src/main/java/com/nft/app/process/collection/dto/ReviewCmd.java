package com.nft.app.process.collection.dto;

import com.nft.domain.common.anno.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReviewCmd {
    @NotNull
    private Integer id; //将要修改的 提交表中的 id
    @NotNull
    @Status(statusType = {"1","2"}) //1 pass ,2 rejection
    private Integer status;
}
