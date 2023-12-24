package com.nft.common.FISCO.Config;

import java.lang.String;
import lombok.Data;


@Data
public class ContractConfig {
  private String sellStroageAddress;

  private String ownershipStorageAddress;

  private String detailStorageAddress;

  private String userStorageAddress;
}

