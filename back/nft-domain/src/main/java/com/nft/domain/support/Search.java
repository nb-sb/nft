package com.nft.domain.support;


import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Search {
    String keyword;

    @NotNull
    @Min(1)
    Integer size;

    Integer page;

    @NotNull
    @Min(1)
    Integer current;
}
