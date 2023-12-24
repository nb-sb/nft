package com.nft.infrastructure.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapStorageCreateMapInputBO {
  private String key;

  private String value;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(key);
    args.add(value);
    return args;
  }
}
