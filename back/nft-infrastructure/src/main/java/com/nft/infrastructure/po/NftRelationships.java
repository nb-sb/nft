package com.nft.infrastructure.po;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 *
 * </p>
 *
 * @author NBSB
 * @since 2023-12-13
 */
@Accessors(chain = true)
@Data
public class NftRelationships implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "藏品id")
    private Integer cid;

//    @ApiModelProperty(value = "分类id")
    private Integer mid;


}
