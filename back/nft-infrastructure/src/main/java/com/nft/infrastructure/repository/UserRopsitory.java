package com.nft.infrastructure.repository;

import com.nft.domain.user.repository.IUserRopsitory;
import org.springframework.stereotype.Repository;

/**
* @author: 戏人看戏
* @Date: 2023/12/8 15:06
* @Description: 仓储实现类
*/
@Repository
public class UserRopsitory implements IUserRopsitory {

    @Override
    public boolean addUser() {
        return false;
    }
}
