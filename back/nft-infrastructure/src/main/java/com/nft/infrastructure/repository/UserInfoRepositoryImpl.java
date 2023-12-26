package com.nft.infrastructure.repository;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.common.Constants;
import com.nft.domain.user.model.req.ChanagePwReq;
import com.nft.domain.user.model.req.LoginReq;
import com.nft.domain.user.model.req.Search;
import com.nft.domain.user.model.req.SignReq;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.infrastructure.dao.UserInfoMapper;
import com.nft.infrastructure.fisco.model.bo.UserStorageAddUserInputBO;
import com.nft.infrastructure.fisco.service.UserStorageService;
import com.nft.infrastructure.po.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
    @Override
    public UserVo selectOne(LoginReq loginReq) {
        UserInfo userInfo = selectOne(loginReq.getUsername(), loginReq.getPassword());
        UserVo userVo = BeanCopyUtils.convertTo(userInfo, UserVo ::new);
        return userVo;
    }

    private UserInfo selectOne(String username,String password ) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("username", username).eq("password", password);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        return userInfo;
    }

    @Override
    public Constants.ResponseCode register(SignReq signReq) {
        //todo 目前只做了用户名,应需判断 手机号 邮箱 等
        boolean exist = isExist(signReq.getUsername());
        if (exist) {
            return Constants.ResponseCode.USER_EXIST;
        }
        UserInfo userInfo = BeanCopyUtils.convertTo(signReq, UserInfo ::new);
        // 创建非国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        // 随机生成非国密公私钥对
        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
        userInfo.setAddress(cryptoKeyPair.getAddress());
        userInfo.setPrivatekey(cryptoKeyPair.getHexPrivateKey());
        int res = userInfoMapper.insert(userInfo);
        if (res > 0) {
            UserStorageAddUserInputBO userInputBO = new UserStorageAddUserInputBO();
            LoginReq loginReq = BeanCopyUtils.convertTo(userInfo, LoginReq ::new);
            UserVo userVo = selectOne(loginReq);
            userInputBO.setUn_id(String.valueOf(userVo.getId()));
            userInputBO.set_address(userVo.getAddress());
            try {
                //调用智能合约添加至区块链中
                TransactionResponse transactionResponse = userStorageService.addUser(userInputBO);
                log.info(transactionResponse.getValues());
            } catch (Exception e) {
                log.error(e);
            }
            return Constants.ResponseCode.SUCCESS;
        }
        return Constants.ResponseCode.UN_ERROR;
    }

    @Override
    public List<UserVo> selectUserPage(Search search) {
        Integer current = search.getCurrent();
        Integer size = search.getSize();
        Page<UserInfo> page = new Page<>(current, size);
        page.setOptimizeCountSql(true);
        Page<UserInfo> userInfoPage = userInfoMapper.selectPage(page, null);
        List<UserInfo> records = userInfoPage.getRecords();
//        System.out.println("===========");
//        System.out.println(userInfoPage.getRecords());
//        System.out.println(userInfoPage.getPages());
//        System.out.println(userInfoPage.getSize());
//        System.out.println(userInfoPage.getCurrent());
//        System.out.println(userInfoPage.getTotal());
        List<UserVo> userVos = BeanCopyUtils.convertListTo(records, UserVo::new);
        return userVos;
    }

    @Override
    public boolean chanagePassword(ChanagePwReq chanagePwReq) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(chanagePwReq.getPassword());
        UpdateWrapper<UserInfo> userWrapper = new UpdateWrapper<>();
        userWrapper.eq("username", chanagePwReq.getUsername());
        int update = userInfoMapper.update(userInfo, userWrapper);
        if (update > 0) {
            return true;
        }else {
            return false;
        }
    }


    //修改密码逻辑，可以用于忘记密码修改和普通的输入原始密码进行修改
    private boolean chanagePassword() {
        return true;
    }

    public boolean isExist(String username) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("username", username);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        if (Optional.ofNullable(userInfo).isPresent()) {
            return true;
        }
        return false;
    }
}
