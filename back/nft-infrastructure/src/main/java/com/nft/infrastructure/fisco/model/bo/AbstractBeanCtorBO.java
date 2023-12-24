package com.nft.infrastructure.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractBeanCtorBO {
  private String tableName;

  private String primaryKey;

  private String uniqueKey;

  private String fields;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(tableName);
    args.add(primaryKey);
    args.add(uniqueKey);
    args.add(fields);
    return args;
  }
}
