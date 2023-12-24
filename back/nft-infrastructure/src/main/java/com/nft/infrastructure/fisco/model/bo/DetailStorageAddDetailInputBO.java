package com.nft.infrastructure.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailStorageAddDetailInputBO {
  private String _transfer_address;

  private String _target_address;

  private String _type;

  private String _hash;

  private String _digital_collection_id;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_transfer_address);
    args.add(_target_address);
    args.add(_type);
    args.add(_hash);
    args.add(_digital_collection_id);
    return args;
  }
}
