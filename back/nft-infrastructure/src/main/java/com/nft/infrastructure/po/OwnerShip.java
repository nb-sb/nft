package com.nft.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 *
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
@Accessors(chain = true)
@Data
public class OwnerShip implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // @ApiModelProperty(value = "所属用户地址")
    private String address;

    // @ApiModelProperty(value = "获得时间")
    private Date time;

    // @ApiModelProperty(value = "数字藏品hash")
    private String hash;

    // @ApiModelProperty(value = "获得类型 type ||  0 表示转增，1表示购买")
    private Integer type;

    // @ApiModelProperty(value = "数字藏品编号")
    private String digitalCollectionId;


}
