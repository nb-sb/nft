package com.nft.domain.nftSort.model.vo;

import com.nft.domain.nftSort.model.req.SortReq;
import lombok.Data;

@Data
public class SortVo extends SortReq {
    private Integer mid;

    //该分类下藏品总数
    private Integer count;
}
