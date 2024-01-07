package com.nft.infrastructure.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 *
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
@Data
@Accessors(chain = true)
@TableName(value = "nft_submit_cache")
public class SubmitCache implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // @ApiModelProperty(value = "文件路径")
    private String path;

    // @ApiModelProperty(value = "出售数量")
    private Integer total;

    // @ApiModelProperty(value = "介绍")
    private String present;

    // @ApiModelProperty(value = "藏品姓名")
    private String name;

    // @ApiModelProperty(value = "作者 所属id")
    private String authorId;

    // @ApiModelProperty(value = "作者 区块链账户地址")
    private String authorAddress;

    // @ApiModelProperty(value = "售价")
    private BigDecimal price;

    // @ApiModelProperty(value = "审核状态 | 0为未审核 ，1 为审核不通过，2为审核成功")
    private Integer status;

    private String hash;

}
