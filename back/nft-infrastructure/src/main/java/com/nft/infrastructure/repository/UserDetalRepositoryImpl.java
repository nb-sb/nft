package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.common.Constants;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.user.model.req.RealNameAuthCmd;
import com.nft.domain.user.model.req.UpdateRealNameAuthStatusCmd;
import com.nft.domain.user.model.vo.RealNameAuthVo;
import com.nft.domain.user.repository.IUserDetalRepository;
import com.nft.infrastructure.dao.UserDetalMapper;
import com.nft.infrastructure.po.UserDetal;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

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
    public boolean submitRealNameAuth(RealNameAuthCmd realNameAuthCmd) {
        UserDetal userDetal = new UserDetal();
        userDetal.setName(realNameAuthCmd.getName())
                .setCardid(realNameAuthCmd.getCardId())
                .setPhoneNumber(realNameAuthCmd.getPhoneNumber())
                .setStatus(Constants.realNameAuthStatus.AWAIT_AUDIT)
                .setAddress(realNameAuthCmd.getAddress())
                .setForId(realNameAuthCmd.getForId());
        int insert = userDetalMapper.insert(userDetal);
        return insert > 0;
    }

    @Override
    public RealNameAuthVo selectByForId(Integer id) {
        QueryWrapper<UserDetal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("for_id", id);
        UserDetal userDetal = userDetalMapper.selectOne(queryWrapper);
        if (userDetal == null) return null;
        RealNameAuthVo realNameAuthVo = BeanCopyUtils.convertTo(userDetal, RealNameAuthVo ::new);
        return realNameAuthVo;
    }

    @Override
    public boolean updataStatusById(UpdateRealNameAuthStatusCmd req) {
        UserDetal userDetal = new UserDetal();
        userDetal.setId(req.getId());
        userDetal.setStatus(req.getStatus());
        int i = userDetalMapper.updateById(userDetal);
        return i > 0;
    }

    @Override
    public RealNameAuthVo selectById(Integer id) {
        UserDetal userDetal = userDetalMapper.selectById(id);
        RealNameAuthVo realNameAuthVo = new RealNameAuthVo();
        realNameAuthVo.setName(userDetal.getName());
        realNameAuthVo.setCardId(userDetal.getCardid());
        realNameAuthVo.setPhoneNumber(userDetal.getPhoneNumber());
        realNameAuthVo.setAddress(userDetal.getAddress());
        realNameAuthVo.setForId(userDetal.getForId());
        realNameAuthVo.setStatus(userDetal.getStatus());
        return realNameAuthVo;
    }

    @Override
    public RealNameAuthVo selectByEmail(String email) {
        LambdaQueryWrapper<UserDetal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDetal::getEmail, email);
        UserDetal userDetal = userDetalMapper.selectOne(queryWrapper);
        if (userDetal == null) return null;
        RealNameAuthVo realNameAuthVo = BeanCopyUtils.convertTo(userDetal, RealNameAuthVo ::new);
        return realNameAuthVo;
    }

    @Override
    public RealNameAuthVo selectByPhone(String phone) {
        LambdaQueryWrapper<UserDetal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDetal::getPhoneNumber, phone);
        UserDetal userDetal = userDetalMapper.selectOne(queryWrapper);
        if (userDetal == null) return null;
        RealNameAuthVo realNameAuthVo = BeanCopyUtils.convertTo(userDetal, RealNameAuthVo ::new);
        return realNameAuthVo;
    }

    @Override
    public RealNameAuthVo selectByPhoneOrEmail(String email,String phone) {
        LambdaQueryWrapper<UserDetal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDetal::getPhoneNumber, phone)
                .or()
                .eq(UserDetal::getEmail,email);
        UserDetal userDetal = userDetalMapper.selectOne(queryWrapper);
        if (userDetal == null) return null;
        RealNameAuthVo realNameAuthVo = BeanCopyUtils.convertTo(userDetal, RealNameAuthVo ::new);
        return realNameAuthVo;
    }

}
