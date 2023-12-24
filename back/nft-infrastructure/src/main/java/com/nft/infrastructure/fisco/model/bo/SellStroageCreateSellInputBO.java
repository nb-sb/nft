package com.nft.infrastructure.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SellStroageCreateSellInputBO {
  private String _hash;

  private BigInteger amount;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_hash);
    args.add(amount);
    return args;
  }
}
