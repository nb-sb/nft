package com.nft.infrastructure.repository;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.infrastructure.dao.UserDetalMapper;
import com.nft.infrastructure.dao.UserInfoMapper;
import com.nft.infrastructure.fisco.model.bo.UserStorageAddUserInputBO;
import com.nft.infrastructure.fisco.service.UserStorageService;
import com.nft.infrastructure.po.UserDetal;
import com.nft.infrastructure.po.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
public class UserInfoRepositoryImpl implements IUserInfoRepository {

    private final UserInfoMapper userInfoMapper;
    private final UserStorageService userStorageService;
    private final UserDetalMapper userDetalMapper;

    @Override
    public UserVo selectOne(String username, String password) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("username", username).eq("password", password);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        if (userInfo == null) {
            return null;
        }
        UserVo userVo1 = new UserVo();
        userVo1.setUsername(userInfo.getUsername());
        userVo1.setAddress(userInfo.getAddress());
        userVo1.setPassword(userInfo.getPassword());
        userVo1.setPrivatekey(userInfo.getPrivatekey());
        userVo1.setBalance(userInfo.getBalance());
        userVo1.setRole(userInfo.getRole());
        userVo1.setId(userInfo.getId());
        return userVo1;
    }

    @Override
    public boolean creat(UserEntity userEntity) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userEntity.getUsername());
        userInfo.setPassword(userEntity.getPassword());
        userInfo.setRole(userEntity.getRole());
        userInfo.setAddress(userEntity.getAddress());
        userInfo.setPrivatekey(userEntity.getPrivatekey());
        userInfo.setBalance(userEntity.getBalance());
        int res = userInfoMapper.insert(userInfo);
        return res > 0;
    }



    @Override
    public boolean addUserByFisco(String id, String address) {
        UserStorageAddUserInputBO userInputBO = new UserStorageAddUserInputBO();
        userInputBO.setUn_id(id);
        userInputBO.set_address(address);
        try {
            //调用智能合约添加至区块链中
            TransactionResponse transactionResponse = userStorageService.addUser(userInputBO);
            log.info(transactionResponse.getValues());
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        return true;
    }

    @Override
    public List<UserEntity> selectUserPage(Page page1) {
        Page<UserInfo> page = new Page<>(page1.getCurrent(), page1.getSize());
        page.setOptimizeCountSql(true);
        Page<UserInfo> userInfoPage = userInfoMapper.selectPage(page, null);
        List<UserInfo> records = userInfoPage.getRecords();
//        System.out.println("===========");
//        System.out.println(userInfoPage.getRecords());
//        System.out.println(userInfoPage.getPages());
//        System.out.println(userInfoPage.getSize());
//        System.out.println(userInfoPage.getCurrent());
//        System.out.println(userInfoPage.getTotal());
        if (records.size() ==0) return null;
        List<UserEntity> userVos = BeanCopyUtils.convertListTo(records, UserEntity::new);
        return userVos;
    }

    @Override
    public boolean saveUserPassword(String username,String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(password);
        UpdateWrapper<UserInfo> userWrapper = new UpdateWrapper<>();
        userWrapper.eq("username", username);
        int update = userInfoMapper.update(userInfo, userWrapper);
        return update > 0;
    }

    @Override
    public UserEntity selectOneById(Integer id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        if (userInfo == null) return null;
        UserVo userVo = new UserVo();
        userVo.setId(userInfo.getId());
        userVo.setUsername(userInfo.getUsername());
        userVo.setAddress(userInfo.getAddress());
        userVo.setPassword(userInfo.getPassword());
        userVo.setPrivatekey(userInfo.getPrivatekey());
        userVo.setBalance(userInfo.getBalance());
        userVo.setRole(userInfo.getRole());
        userVo.setId(userInfo.getId());
        return userVo;
    }

    @Override
    public boolean saveBalance(UserEntity userEntity) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userEntity.getId());
        userInfo.setBalance(userEntity.getBalance());
        int i = userInfoMapper.updateById(userInfo);
        return i>0;
    }

    @Override
    public UserInfoVo selectUserDetail(Integer forid) {
        QueryWrapper<UserDetal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("for_id", forid);
        UserDetal userDetal = userDetalMapper.selectOne(queryWrapper);
        if (userDetal == null) return null;
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setId(userDetal.getId());
        userInfoVo.setAddress(userDetal.getAddress());
        userInfoVo.setCardId(userDetal.getCardid());
        userInfoVo.setPhoneNumber(userDetal.getPhoneNumber());
        return userInfoVo;
    }




    //修改密码逻辑，可以用于忘记密码修改和普通的输入原始密码进行修改
    private boolean chanagePassword() {
        return true;
    }
    @Override
    public boolean isUserNameExist(String username) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("username", username);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        if (Optional.ofNullable(userInfo).isPresent()) return true;
        return false;
    }
    @Override
    public UserVo selectUserName(String username) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("username", username);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        UserVo userVo = BeanCopyUtils.convertTo(userInfo, UserVo ::new);
        return userVo;
    }
}
