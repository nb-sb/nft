package com.nft.domain.user.model.entity;

import com.nft.domain.user.model.vo.AuthCodeCheckTypeVO;
import lombok.*;

/**
* @author: 戏人看戏
* @Date: 2024/1/18 19:51
* @Description: 验证码 动作实体
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthCodeActionEntity<T extends AuthCodeActionEntity.AuthEntity>  {
    private String code = AuthCodeCheckTypeVO.ALLOW.getCode();
    private String info = AuthCodeCheckTypeVO.ALLOW.getInfo();
    private String authModel;
    private T data;
    static public class AuthEntity {

    }

    // 邮箱验证
    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class AuthEmailEntity extends AuthEntity {
        /**
         * 验证结果
         */
        private boolean code;
        private String res;
    }

    // 抽奖之中
    static public class AuthPhoneEntity extends AuthEntity {

    }
}
