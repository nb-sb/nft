package com.nft.app.process.user;

import com.nft.app.process.user.dto.CreatCmd;
import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.domain.user.model.entity.UserEntity;
import com.nft.domain.user.model.req.ChanagePwCmd;
import com.nft.domain.user.model.req.RealNameAuthCmd;
import com.nft.domain.user.model.req.UpdateRealNameAuthStatusCmd;
import com.nft.domain.user.model.res.UserResult;
import com.nft.domain.user.model.vo.RealNameAuthVo;
import com.nft.domain.user.repository.IUserDetalRepository;
import com.nft.domain.user.repository.IUserInfoRepository;
import com.nft.domain.user.service.Factory.UserEntityFatory;
import com.nft.domain.user.service.VerifyCode.VerifyFactory;
import lombok.AllArgsConstructor;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UserCommandService {
    private final IUserInfoRepository iUserInfoRepository;
    private final IUserDetalRepository iUserDetalRepository;
    private final  UserEntityFatory userEntityFatory;
    private final VerifyFactory verifyFactory;;
    @Transactional
    public UserResult creat(CreatCmd cmd) {
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
        userEntity.userRole();//设置普通用户权限
        boolean res = iUserInfoRepository.creat(userEntity);
        UserEntity userVo = iUserInfoRepository.selectOne(cmd.getUsername(), cmd.getPassword());
        //添加至fisco中存贮
        iUserInfoRepository.addUserByFisco(String.valueOf(userVo.getId()), userVo.getAddress());
        if (res) {
            return new UserResult("1", "注册成功");
        }
        return new UserResult("0", "注册失败");
    }


//    public void test() {
//        UserEntity userEntity = userEntityFatory.newInstance("cmd.getUsername()", "cryptoKeyPair.getAddress()",
//                "cmd.getPassword()", "cryptoKeyPair.getHexPrivateKey()", BigDecimal.valueOf(0),0);
//        //添加数据库存贮
//        boolean res = iUserInfoRepository.creat(userEntity);
//        System.out.println(res);
//        iUserInfoRepository.test();
//    }
    /**
     * @Des 修改密码逻辑
     * {
     *     //使用旧密码修改密码
     *     String username;
     *     String password;
     *     String oldpassword;
     *     String type; // 验证类型 2 是使用 使用验证码修改 , 1 是使用旧密码修改
     * }
     * {
     *     //使用邮箱或手机号密码验证需要传入的参数
     *     String username;
     *     String password;
     *     String phone;  OR  String email;
     *     String code; //验证码
     *     String type; // 验证类型 2 是使用 使用验证码修改 , 1 是使用旧密码修改
     * }
     */
    public Result changePassword(ChanagePwCmd cmd) {
        //判断逻辑 - 1.判断验证码是否正确 2.判断验证码是否合规
        Integer type = cmd.getType();
        //需要登录后才能使用旧密码修改
        if (Constants.USE_OLDPW.equals(type)) {
            if (cmd.getUsername() == null) return UserResult.error("需要登录后才能使用旧密码修改");
        }
        cmd.loadTarget();
        Result verifyService = verifyFactory.getVerifyService(cmd.getType(),cmd.getInput_key(),cmd.getTarget());
        //如果验证失败则返回失败信息
        if (verifyService.getCode().equals("0")) {
            return verifyService;
        }
        if (!Constants.USE_OLDPW.equals(type)){
            //1.判断验证类型
            //使用邮箱/手机号反查到这人,然后在使用这个用户名 进行修改这个用户的密码
            RealNameAuthVo realNameAuthVo = iUserDetalRepository.selectByPhoneOrEmail(cmd.getEmail(), cmd.getPhone());
            if (realNameAuthVo == null) return Result.error("输入错误，请检查邮箱或手机号是否正确");
            UserEntity userEntity = iUserInfoRepository.selectOneById(realNameAuthVo.getForId());
            cmd.setUsername(userEntity.getUsername());
        }
        // 修改密码操作
        boolean b = iUserInfoRepository.saveUserPassword(cmd.getUsername(),
                cmd.getPassword());
        if (!b) return UserResult.error("修改失败");
        return Result.success("修改成功!");
    }
    public Result RealNameAuth(RealNameAuthCmd realNameAuthCmd) {
        realNameAuthCmd.setAddress(realNameAuthCmd.getAddress());
        realNameAuthCmd.setForId(realNameAuthCmd.getForId());
        //判断自己是否已经存在了认证信息，存在则无需提交
        RealNameAuthVo realNameAuthVo = iUserDetalRepository.selectByForId(realNameAuthCmd.getForId());
        if (realNameAuthVo != null) {
            return Result.error("有待审核的认证，请等待审核！");
        }
        if (iUserDetalRepository.submitRealNameAuth(realNameAuthCmd))return new Result("1","提交成功");
        return Result.error("提交失败");
    }

    public Result AuditRealNameAuth(UpdateRealNameAuthStatusCmd cmd) {
        RealNameAuthVo realNameAuthReq = iUserDetalRepository.selectById(cmd.getId());
        if (realNameAuthReq == null) {
            //返回 审核的id不存在
            return Result.error("审核的id不存在");
        }
        //判断状态需要为待审核才能进行修改
        if (!Constants.realNameAuthStatus.AWAIT_AUDIT.equals(realNameAuthReq.getStatus())) {
            //返回 该内容已经被审核
            return Result.error("该信息已经被审核");
        }
        boolean b = iUserDetalRepository.updataStatusById(cmd);
        return Result.success("成功");
    }

}
