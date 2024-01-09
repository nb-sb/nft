package com.nft.domain.nft.model.res;

import com.nft.common.Result;
import com.nft.domain.user.model.res.SelectRes;
import lombok.Data;

import java.io.Serializable;


@Data
public class AuditRes extends Result implements Serializable {

    public AuditRes() {
        super();
    }

    public AuditRes(Integer code, String info) {
        super(String.valueOf(code), info);
    }
    public static AuditRes success(String info) {
        return new AuditRes(1, info);
    }
    public static AuditRes error(String info) {
        return new AuditRes(0, info);
    }
}
