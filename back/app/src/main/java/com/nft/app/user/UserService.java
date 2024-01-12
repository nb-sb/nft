package com.nft.app.user;

import cn.hutool.json.JSONUtil;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisConstant;
import com.nft.common.Redis.RedisUtil;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.app.user.dto.CreatCmd;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.UserInfoVo;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserDetalRepository;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.Factory.VerifyCode.VerifyFactory;
import com.nft.domain.user.service.Factory.UserEntityFatory;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    IUserInfoRepository iUserInfoRepository;
    @Autowired
    IUserDetalRepository iUserDetalRepository;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    Token2User token2User;
    @Autowired
    VerifyFactory verifyFactory;
    @Resource
    private UserEntityFatory userEntityFatory;

    public UserResult creat(CreatCmd cmd) {
        cmd.setRole(0);//设置普通用户权限
        //判断用户名是否存在
        boolean userNameExist = iUserInfoRepository.isUserNameExist(cmd.getUsername());
        if (userNameExist) {
            return new UserResult("0", Constants.ResponseCode.USER_EXIST.getInfo());
        }
        // 创建非国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        // 随机生成非国密公私钥对
        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
        UserEntity userEntity = userEntityFatory.newInstance(cmd.getUsername(), cryptoKeyPair.getAddress(),
                cmd.getPassword(), cryptoKeyPair.getHexPrivateKey(), BigDecimal.valueOf(0), cmd.getRole());
        //添加数据库存贮
        boolean res = iUserInfoRepository.creat(userEntity);
        UserVo userVo = iUserInfoRepository.selectOne(cmd.getUsername(), cmd.getPassword());
        //添加至fisco中存贮
        iUserInfoRepository.addUserByFisco(String.valueOf(userVo.getId()), userVo.getAddress());
        if (res) {
            return new UserResult("1", "注册成功");
        }
        return new UserResult("0", "注册失败");
    }
    public UserInfoVo selectUserAllInfo(UserVo userOne) {
        //查询用户个人信息。由于个人信息基本是不变的所以可以直接存入redis中
        String key = Constants.RedisKey.USER_INFO(userOne.getId());
        UserInfoVo userDetailByRedis = getUserDetailByRedis(key);
        if (userDetailByRedis != null) {
            return userDetailByRedis;
        }
        UserInfoVo userInfoVo = iUserInfoRepository.selectUserDetail(userOne.getId());
        if (userInfoVo == null) {
            return null;
        }
        userInfoVo.setUsername(userOne.getUsername());
        userInfoVo.setPassword("******************");
        userInfoVo.setRole(userOne.getRole());
        userInfoVo.setBalance(userOne.getBalance());
        redisUtil.set(key, JSONUtil.toJsonStr(userInfoVo), RedisConstant.DAY_ONE);
        return userInfoVo;
    }
    private UserInfoVo getUserDetailByRedis(String key) {
        String userInfoStr = redisUtil.getStr(key);
        UserInfoVo bean = JSONUtil.toBean(userInfoStr, UserInfoVo.class);
        return bean;
    }
}
