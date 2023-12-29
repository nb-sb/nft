package com.nft.domain.nftSort.model.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateSortReq extends SortReq {
    @NotNull()
    @Min(1)
    Integer mid;
}
