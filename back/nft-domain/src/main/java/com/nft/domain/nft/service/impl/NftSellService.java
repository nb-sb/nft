package com.nft.domain.nft.service.impl;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.nft.common.Constants;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Result;
import com.nft.common.Utils.TokenUtils;
import com.nft.common.Utils.VerifyUtil;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.model.vo.SubCacheVo;
import com.nft.domain.nft.repository.INftSellRespository;
import com.nft.domain.nft.service.INftSellService;
import com.nft.domain.support.ipfs.IpfsService;
import com.nft.domain.support.redis.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;


@Log4j2
@Service
@AllArgsConstructor
public class NftSellService implements INftSellService {

    private  final  INftSellRespository iNftSellRespository;
    private  final RedisUtil redisUtil;
    private final IpfsService ipfsService;
    @Override
    public NftRes addSellCheck(HttpServletRequest httpServletRequest, SellReq sellReq) {
        String token = httpServletRequest.getHeader("token");
        Optional<String> tokenOpt = Optional.ofNullable(token);
        if (!tokenOpt.isPresent() ) {
            System.err.println("token为空");
        }
        try {
            if (!TokenUtils.verify(tokenOpt.get())) {
                return new NftRes("0", "token无效");
            }
        } catch (JWTDecodeException e) {
            System.err.println(e);
            return new NftRes("0", "token无效");
        }

        try {
            VerifyUtil.checkBean(sellReq, "传入的参数有空值！");
        } catch (Exception e) {
            System.err.println(e);
            return new NftRes("0", String.valueOf(e));
        }
        Map<String, String> userMap = TokenUtils.decodeToken(tokenOpt.get());
        return iNftSellRespository.addSellCheck(sellReq, userMap);
    }

    @Override
    public AuditRes changeSellStatus(ReviewReq req) {
        //判断修改前后变量结果是否一致，如果修改前后都是同一状态则不修改
        SubCacheVo subCacheVo = iNftSellRespository.selectSubSellById(req.getId());
        if (!Constants.SellState.DOING.equals(subCacheVo.getStatus())) {
            return new AuditRes(Constants.SellState.NOTDOING.getCode(), Constants.SellState.NOTDOING.getInfo());
        }
        if (subCacheVo.getStatus().equals(req.getStatus())) {
            log.warn("修改前后状态一致！");
            return new AuditRes(Constants.SellState.ERROR.getCode(), Constants.SellState.ERROR.getInfo());
        }
        if (Constants.SellState.REFUSE.getCode().equals(req.getStatus())) {
            //如果是不同意的话这里可以直接修改审核结果然后返回到controller中
            iNftSellRespository.upDateSubStatus(req);
            return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
        }
        if (Constants.SellState.PASS.getCode().equals(req.getStatus())) {
            return new AuditRes(Constants.SellState.PASS.getCode(), Constants.SellState.PASS.getInfo());
        }
        return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
    }


    @Override
    public boolean insertSellInfo(ReviewReq req, String hash) {
        boolean b = iNftSellRespository.upDateSubStatus(req); //修改提交表
        boolean b1 = iNftSellRespository.insertSellInfo(req.getId(), hash); //增加出售表
        return b && b1;
    }

    @Override
    public boolean addSellByFISCO(String hash,Integer id) {
        return iNftSellRespository.addSellByFISCO(hash, id);
    }


    @Override
    public void purchaseConllection() {
        String id = "id123"; //商品id
        //1.添加写锁
        DistributedRedisLock.acquireWriteLock("id");
        try {
            //2.购买藏品操作
            //1)purcharseConllection
            // 3.减少库存操作
            //1)decrRemain
            //2）更新redis和es中数据 // 这里实际上可以直接将缓存给删掉，等下次查询的时候自动更新最新的，无需修改
        } finally{
            //释放写锁
            DistributedRedisLock.releaseWriteLock("id");
        }

    }





    @Override
    public Result updataConllectionInfo(UpdataCollectionReq updataCollectionReq) {
        //1.添加写锁
        DistributedRedisLock.acquireWriteLock(String.valueOf(updataCollectionReq.getId()));
        try {
            //2.更新藏品信息
                //1)updataConllectionInfo
            if (updataCollectionReq.getName() !=null || updataCollectionReq.getPresent() !=null) {
                boolean b = iNftSellRespository.updataConllectionInfo(updataCollectionReq);//更新藏品信息
                return b ? new Result("1", "更新藏品信息成功!") : new Result("0", "更新藏品信息失败!");
            }
            if (updataCollectionReq.getStatus() != null) {
                boolean b = iNftSellRespository.updataSellStatus(updataCollectionReq);// 如果有状态的话则会更新出售表中状态
                return b ? new Result("1", "更新出售状态成功!") : new Result("0", "更新出售状态失败!");
            }
            return new Result("0", "什么都没执行");
        } finally{
            // 3.更新缓存数据
            //1）更新redis和es中数据 // 这里实际上可以直接将缓存给删掉，等下次查询的时候自动更新最新的，无需修改
            String reidsKey = Constants.RedisKey.REDIS_COLLECTION(updataCollectionReq.getId());
            redisUtil.del(reidsKey);
            //释放写锁
            DistributedRedisLock.releaseWriteLock(String.valueOf(updataCollectionReq.getId()));
        }
    }

    @Override
    public AuditRes ReviewCollection(ReviewReq req) {
        //TODO:这里需要写入数据操作，应考虑并发情况（2个管理员同时更新商品）
        // 需要先修改区块链状态在修改mysql状态！！
        //2.操作审核状态 （1 为不通过 2为通过）
        AuditRes auditRes = changeSellStatus(req);
        if (!String.valueOf(Constants.SellState.PASS.getCode()).equals(auditRes.getCode())) {
            //如果是不通过则这里直接返回参数，无需进行下面操作
            return auditRes;
        }
        // -- 通过：通过才会加入到出售表，ipfs,区块链数据--
        //3.添加至ipfs 获得hash
        String hash = ipfsService.addIpfsById(String.valueOf(req.getId()));
        System.err.println("hash : "+hash);
        //4.添加至区块链
        boolean addFISCO = addSellByFISCO(hash, req.getId());
        System.out.println(addFISCO);
        if (!addFISCO) {
            return new AuditRes(Constants.SellState.ERRORFISCO.getCode(), Constants.SellState.ERRORFISCO.getInfo());
        }
        //5.保存审核内容至mysql 出售表中 - 1.修改提交表结果 2.修改出售表结果 上架出售
        if (!insertSellInfo(req, hash)) {
            return new AuditRes(Constants.SellState.ERROR.getCode(), Constants.SellState.ERROR.getInfo());
        }
        return new AuditRes(Constants.SellState.PASS.getCode(), Constants.SellState.PASS.getInfo());
    }



    public void decrRemain() {
        //传入商品id和减少数量，默认减少1个库存量
        //1）更新区块链上数据
        //2）更新mysql上数据
    }
    public void purcharseConllection() {
        //1）更新区块链上数据
        //2）更新mysql上数据
    }
    public void updataConllectionInfo() {
        //传入更新对象，用于更新出售商品数据更新数据
    }



}
