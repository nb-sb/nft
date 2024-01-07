package com.nft.infrastructure.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
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

@Data
@Accessors(chain = true)
@TableName(value = "nft_relationships")
public class NftRelationships implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer cid;


    private Integer mid;


}
