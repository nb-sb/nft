package com.nft.app.process.sort.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UpdataCmd {
    @NotNull()
    @Min(1)
    Integer mid;
    @NotNull
    private String name;
    @NotNull
    private String slug;
}
