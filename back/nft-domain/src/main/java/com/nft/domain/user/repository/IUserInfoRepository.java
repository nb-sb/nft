package com.nft.domain.user.repository;


import com.nft.common.Constants;
import com.nft.domain.user.model.req.ChanagePwReq;
import com.nft.domain.user.model.req.LoginReq;
import com.nft.domain.support.Search;
import com.nft.domain.user.model.req.RealNameAuthReq;
import com.nft.domain.user.model.req.SignReq;
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

    Constants.ResponseCode register(SignReq signReq);

    List<UserVo> selectUserPage(Search search);

    boolean chanagePassword(ChanagePwReq chanagePwReq);

    UserVo selectUserByid(Integer id);

    //修改用户余额
    boolean decrementUserBalance(Integer id, BigDecimal balance);

    UserInfoVo selectUserDetail(Integer forid);


}
