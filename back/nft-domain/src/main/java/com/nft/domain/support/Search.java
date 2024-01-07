package com.nft.domain.support;


import com.nft.common.PageRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Search extends PageRequest {
    String keyword;
}
