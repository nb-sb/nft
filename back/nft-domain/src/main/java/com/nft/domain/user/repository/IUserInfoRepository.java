package com.nft.domain.user.repository;


import com.nft.common.Constants;
import com.nft.domain.user.model.req.ChanagePwReq;
import com.nft.domain.user.model.req.LoginReq;
import com.nft.domain.user.model.req.Search;
import com.nft.domain.user.model.req.SignReq;
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

    UserVo selectOne(LoginReq loginReq);

    Constants.ResponseCode register(SignReq signReq);

    List<UserVo> selectUserPage(Search search);

    boolean chanagePassword(ChanagePwReq chanagePwReq);
}
