package com.nft.infrastructure.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableUpdateInputBO {
  private String arg0;

  private String arg1;

  private String arg2;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(arg0);
    args.add(arg1);
    args.add(arg2);
    return args;
  }
}
