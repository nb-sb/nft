package com.nft.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;


/**
 * <p>
 *
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
// @ApiModel(value="OwnerShip对象", description="")
public class OwnerShip implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // @ApiModelProperty(value = "所属用户地址")
    private String address;

    // @ApiModelProperty(value = "获得时间")
    private LocalDateTime time;

    // @ApiModelProperty(value = "数字藏品hash")
    private String hash;

    // @ApiModelProperty(value = "获得类型 type ||  0 表示转增，1表示购买")
    private Integer type;

    // @ApiModelProperty(value = "数字藏品编号")
    private Integer digitalCollectionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getDigitalCollectionId() {
        return digitalCollectionId;
    }

    public void setDigitalCollectionId(Integer digitalCollectionId) {
        this.digitalCollectionId = digitalCollectionId;
    }

    @Override
    public String toString() {
        return "OwnerShip{" +
            "id=" + id +
            ", address=" + address +
            ", time=" + time +
            ", hash=" + hash +
            ", type=" + type +
            ", digitalCollectionId=" + digitalCollectionId +
        "}";
    }
}
