package com.nft.domain.order.model.res;

import com.nft.common.Constants;
import com.nft.common.Result;
import lombok.Data;

@Data
public class OrderRes extends Result {
    Object data;
    public OrderRes(String code, String info, Object data) {
        super(code, info);
        this.data = data;
    }
    public static OrderRes success(Object data) {
        return new OrderRes(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), data);
    }
}

