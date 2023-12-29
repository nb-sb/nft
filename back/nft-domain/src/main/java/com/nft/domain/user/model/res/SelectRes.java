package com.nft.domain.user.model.res;

import com.nft.common.Result;
import com.nft.domain.nft.model.res.GetNftRes;
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
    public static SelectRes success(Object data) {
        return new SelectRes("1", "success", data);
    }
    public static SelectRes error(String info) {
        return new SelectRes("0", info);
    }
}
