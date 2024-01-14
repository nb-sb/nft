package com.nft.domain.user.repository;

import com.nft.domain.user.model.req.RealNameAuthCmd;
import com.nft.domain.user.model.req.UpdateRealNameAuthStatusCmd;
import com.nft.domain.user.model.vo.RealNameAuthVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
public interface IUserDetalRepository {

    boolean submitRealNameAuth(RealNameAuthCmd realNameAuthCmd);

    RealNameAuthVo selectByForId(Integer id);

    boolean updataStatusById(UpdateRealNameAuthStatusCmd req);

    RealNameAuthVo selectById(Integer id);

    RealNameAuthVo selectByEmail(String email);

    RealNameAuthVo selectByPhone(String phone);
    RealNameAuthVo selectByPhoneOrEmail(String email,String phone);
}
