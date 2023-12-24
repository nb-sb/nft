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
public class DetailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

//    // @ApiModelProperty(value = "藏品hash")
    private String hash;

//    @ApiModelProperty(value = "转移方用户地址")
    private String transferAddress;

//    @ApiModelProperty(value = "接受方用户地址")
    private String targetAddress;

//    @ApiModelProperty(value = "0 表示转增，1表示购买 ||用于藏品来源显示")
    private String type;

//    @ApiModelProperty(value = "时间")
    private LocalDateTime time;

//    @ApiModelProperty(value = "数字藏品编号	例如 1#5000 或 51#5000 等也就是id和总数进行拼接")
    private String digitalCollectionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    public String getTransferAddress() {
        return transferAddress;
    }

    public void setTransferAddress(String transferAddress) {
        this.transferAddress = transferAddress;
    }
    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public String getDigitalCollectionId() {
        return digitalCollectionId;
    }

    public void setDigitalCollectionId(String digitalCollectionId) {
        this.digitalCollectionId = digitalCollectionId;
    }

    @Override
    public String toString() {
        return "DetailInfo{" +
            "id=" + id +
            ", hash=" + hash +
            ", transferAddress=" + transferAddress +
            ", targetAddress=" + targetAddress +
            ", type=" + type +
            ", time=" + time +
            ", digitalCollectionId=" + digitalCollectionId +
        "}";
    }
}
