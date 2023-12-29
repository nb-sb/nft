package com.nft.domain.user.model.res;

import com.nft.common.Result;
import lombok.Data;

@Data
public class SelectRes extends Result  {
    Object data;
    public SelectRes(String code, String info) {
        super(code, info);
    }

    public SelectRes(String code, String info, Object data) {
        super(code, info);
        this.data = data;
    }
}
