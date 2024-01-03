package com.nft.domain.nft.model.vo;

import lombok.Data;

import java.util.Date;
@Data
public class OwnerShipVo {
    Integer id;
    String address;
    Date time;
    String hash;
    Integer type;
    String digitalCollectionId;
}
