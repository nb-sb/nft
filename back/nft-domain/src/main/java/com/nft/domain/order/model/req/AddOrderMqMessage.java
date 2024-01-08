package com.nft.domain.order.model.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AddOrderMqMessage {
    Integer conllectionId;
    Integer userId;
    Date time;
}
