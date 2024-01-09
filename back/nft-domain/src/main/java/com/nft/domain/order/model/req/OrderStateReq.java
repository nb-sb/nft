package com.nft.domain.order.model.req;

import com.nft.domain.common.anno.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderStateReq {
    @Status(statusType = {"1","2","3","4","5"})
    Integer status;
}
