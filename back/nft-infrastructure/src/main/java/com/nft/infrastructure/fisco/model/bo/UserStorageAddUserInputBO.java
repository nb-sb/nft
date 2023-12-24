package com.nft.infrastructure.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStorageAddUserInputBO {
  private String un_id;

  private String _address;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(un_id);
    args.add(_address);
    return args;
  }
}
