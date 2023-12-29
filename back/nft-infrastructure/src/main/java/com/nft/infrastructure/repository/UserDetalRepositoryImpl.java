package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.common.Constants;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.user.model.req.RealNameAuthReq;
import com.nft.domain.user.model.vo.RealNameAuthVo;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserDetalRepository;
import com.nft.infrastructure.dao.UserDetalMapper;
import com.nft.infrastructure.po.UserDetal;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
@Repository
@Log4j2
@AllArgsConstructor
public class UserDetalRepositoryImpl implements IUserDetalRepository {
    private final UserDetalMapper userDetalMapper;

    @Override
    public boolean submitRealNameAuth(RealNameAuthReq realNameAuthReq) {
        UserDetal userDetal = new UserDetal();
        userDetal.setName(realNameAuthReq.getName())
                .setCardid(realNameAuthReq.getCardId())
                .setPhoneNumber(realNameAuthReq.getPhoneNumber())
                .setStatus(Constants.realNameAuthStatus.awaiting_audit)
                .setAddress(realNameAuthReq.getAddress())
                .setForId(realNameAuthReq.getForid());
        int insert = userDetalMapper.insert(userDetal);
        return insert > 0;
    }

    @Override
    public RealNameAuthVo selectByForId(Integer id) {
        QueryWrapper<UserDetal> queryWrapper = new QueryWrapper<>();
        UserDetal userDetal = userDetalMapper.selectOne(queryWrapper);
        RealNameAuthVo realNameAuthVo = BeanCopyUtils.convertTo(userDetal, RealNameAuthVo ::new);
        return realNameAuthVo;
    }

}
