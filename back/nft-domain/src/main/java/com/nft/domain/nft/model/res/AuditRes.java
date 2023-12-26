package com.nft.domain.nft.model.res;

import com.nft.common.Result;
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
}
