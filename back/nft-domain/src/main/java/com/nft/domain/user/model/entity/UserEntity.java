package com.nft.domain.user.model.entity;

import com.nft.common.APIException;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@Accessors(chain = true)
public class UserEntity {
    private Integer id;
    private String username;
    private String address;
    private String password;
    private String privatekey;
    private BigDecimal balance;
    private Integer role;
    public void increaseBalance(BigDecimal amount) {
        // 添加一些业务规则，例如余额不能为负等
        this.balance = this.balance.add(amount);
    }
    //减少余额
    public void decreaseBalance(BigDecimal amount) {
        // 添加一些业务规则，例如余额不能为负等
        if (this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
        } else {
            // 处理余额不足的情况，可以抛出异常或采取其他适当的处理方式
            throw new APIException("Insufficient balance.");
        }
    }

    public void desensitisationPassword() {
        this.password = "*********";
    }
    public void userRole() {
        this.role = 0;
    }

}
