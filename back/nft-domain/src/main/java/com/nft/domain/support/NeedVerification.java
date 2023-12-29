package com.nft.domain.support;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NeedVerification {
    @NotNull(message = "the paramter cannot be null")
    String codeId;
}
