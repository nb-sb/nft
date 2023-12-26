package com.nft.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 *
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
@Data
// @ApiModel(value="UserInfo对象", description="")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String address;

    private String username;

    private String password;
    private String privatekey;
    private BigDecimal balance;
    private Integer role;

}
