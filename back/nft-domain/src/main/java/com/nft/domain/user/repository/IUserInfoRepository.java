package com.nft.domain.user.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.model.req.ChanagePwReq;
import com.nft.domain.user.model.req.LoginReq;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;

import java.math.BigDecimal;
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

    UserVo selectOne(LoginReq loginReq);

    UserVo selectOne(String username, String password);

    boolean creat(UserEntity userEntity);

    boolean addUserByFisco(String id, String address);

    List<UserVo> selectUserPage(Page page);

    boolean chanagePassword(ChanagePwReq chanagePwReq);

    UserVo selectUserByid(Integer id);

    //修改用户余额
    boolean decrementUserBalance(Integer id, BigDecimal balance);

    UserInfoVo selectUserDetail(Integer forid);


    boolean isUserNameExist(String username);
}
