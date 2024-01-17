package com.nft.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
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
@TableName(value = "nft_sell_info")
public class SellInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // @ApiModelProperty(value = "提交的缓存表的对应的id")
    private Integer uniqueId;

    // @ApiModelProperty(value = "藏品hash")
    private String hash;

    // @ApiModelProperty(value = "发行量")
    private Integer amount;

    // @ApiModelProperty(value = "剩余数量")
    private Integer remain;

    // @ApiModelProperty(value = "数字藏品作者地址")
    private String auther;

    // @ApiModelProperty(value = "# 1 为正常 ，  0 为闭售")
    private Integer status;
    private String ipfsHash;

}
