package com.nft.domain.nftSort.model.res;

import com.nft.common.Result;
import lombok.Data;

@Data
public class SortRes extends Result {
    Object data;
    public SortRes(String code, String info, Object data) {
        super(code, info);
        this.data = data;
    }

    public static SortRes success(Object data) {
        return new SortRes("1", "success", data);
    }
}
