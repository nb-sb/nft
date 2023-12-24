package com.nft.infrastructure.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnershipStorageTransferInputBO {
  private String target_address;

  private String _hash;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(target_address);
    args.add(_hash);
    return args;
  }
}
