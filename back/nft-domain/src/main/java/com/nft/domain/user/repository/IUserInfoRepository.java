package com.nft.domain.user.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
public interface IUserInfoRepository {


    UserVo selectOne(String username, String password);

    boolean creat(UserEntity userEntity);
    void test();

    boolean addUserByFisco(String id, String address);

    List<UserEntity> selectUserPage(Page page);

    boolean saveUserPassword(String username,String password);

    UserEntity selectOneById(Integer id);

    //修改用户余额
    boolean saveBalance(UserEntity userEntity);

    UserInfoVo selectUserDetail(Integer forid);


    boolean isUserNameExist(String username);

    UserVo selectUserName(String username);
}
