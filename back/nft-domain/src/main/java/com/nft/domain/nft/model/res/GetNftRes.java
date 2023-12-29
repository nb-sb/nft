package com.nft.domain.nft.model.res;

import com.nft.common.Constants;
import com.nft.common.Result;
import lombok.Data;

@Data
public class GetNftRes extends Result {
    Object data;
    public GetNftRes(String code, String info, Object data) {
        super(code, info);
        this.data = data;
    }

    public static GetNftRes success(Object data) {
        return new GetNftRes(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), data);
    }
}
