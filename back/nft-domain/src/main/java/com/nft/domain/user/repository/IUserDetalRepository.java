package com.nft.domain.user.repository;

import com.nft.domain.user.model.req.RealNameAuthReq;
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

    boolean submitRealNameAuth(RealNameAuthReq realNameAuthReq);

    RealNameAuthVo selectByForId(Integer id);
}
