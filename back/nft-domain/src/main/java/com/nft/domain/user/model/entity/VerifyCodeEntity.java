package com.nft.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeEntity {

    String target;

    String input_key;

}
