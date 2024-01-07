package com.nft.infrastructure.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
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
/*@ApiModel(value="NftMetas对象", description="")*/
@Accessors(chain = true)
@Data
@TableName(value = "nft_metas")
public class NftMetas implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "藏品分类id")
    private Integer mid;

//    @ApiModelProperty(value = "藏品分类名称")
    private String conllectionName;

//    @ApiModelProperty(value = "藏品分类代号")
    private String conllectionSlug;

//    @ApiModelProperty(value = "该分类下藏品总数")
    private Integer count;


}
