package com.nft.infrastructure.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnershipStorageSelectByUserAddrInputBO {
  private String _address;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_address);
    return args;
  }
}
