package com.nft.app.process.collection;

import com.nft.app.process.collection.dto.TransferCmd;
import com.nft.app.mq.producer.OrderPublisher;
import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Result;
import com.nft.common.Utils.TimeUtils;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.vo.DetailInfoVo;
import com.nft.domain.nft.model.vo.OwnerShipVo;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.domain.nft.repository.ISellInfoRespository;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@AllArgsConstructor
public class CollectionCommandService {
    private final ISubmitCacheRespository iSubmitCacheRespository;
    private final RedisUtil redisUtil;
    private final OrderPublisher orderPublisher;
    private final IOwnerShipRespository iOwnerShipRespository;
    private final ISellInfoRespository iSellInfoRespository;
    private final DistributedRedisLock distributedRedisLock;

    @Transactional
    public Result transferCollection(TransferCmd cmd) {
        String privatekey = cmd.getUserPrivatekey();
        String fromAddress = cmd.getUserAddress();
        String toAddress = cmd.getToAddress();
        Integer id = cmd.getId(); //所属藏品id
        if (StringUtil.equals(fromAddress, toAddress)) {
            return Result.error("不能将藏品转移给自己");
        }
        // TODO: 2024/1/4 判断toAddress是否存在
        //验证是否是自己的藏品
        OwnerShipVo myConllection = iOwnerShipRespository.getMyConllection(id, fromAddress);
        if (myConllection == null) {
            //返回 藏品不是你的
            return Result.error("该藏品不是你的，或藏品不存在");
        }
        boolean b1 = iOwnerShipRespository.transferCollectionByFisco(privatekey,myConllection.getHash(), toAddress, id);
        if (!b1) {
            log.error("区块链中：藏品转移错误");
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "区块链中：藏品转移错误");
        }
        //更新数据库中所属数据
        boolean b = iOwnerShipRespository.transferCollection(fromAddress, toAddress, id);
        if (!b) {
            log.error("更新流水表进行记录错误: "+fromAddress+","+ toAddress+","+id);

        }

        //调用区块链中转移方法 -- 合约中自动添加流水表 //使用所有者进行调用
        //更新msql流水表进行记录
        DetailInfoVo detailInfoVo = new DetailInfoVo();
        detailInfoVo.setTime(TimeUtils.getCurrent())
                .setHash(myConllection.getHash())
                .setTransferAddress(myConllection.getAddress())
                .setTargetAddress(toAddress)
                .setDigitalCollectionId(myConllection.getDigitalCollectionId())
                .setType(Constants.CollectionOwnerShipType.transfer);
        orderPublisher.SendDetailInfo(detailInfoVo);
        return Result.success("藏品转移成功！");
    }
    //修改商品信息 -1.可以在用户购买商品的时候用于更新剩余信息等 2.管理员更新藏品信息的时候用
    public Result updataConllectionInfo(UpdataCollectionReq updataCollectionReq) {
        //1.添加写锁
        distributedRedisLock.acquireWriteLock(Constants.RedisKey.READ_WRITE_LOCK(updataCollectionReq.getId()));
        try {
            //2.更新藏品信息
            //1)updataConllectionInfo
            if (updataCollectionReq.getName() != null || updataCollectionReq.getPresent() != null) {
                boolean b = iSellInfoRespository.updataConllectionInfo(updataCollectionReq);//更新藏品信息
                return b ? Result.success("更新藏品信息成功!") : Result.error("更新藏品信息失败!");
            }
            if (updataCollectionReq.getStatus() != null) {
                boolean b = iSubmitCacheRespository.updataSellStatus(updataCollectionReq);// 如果有状态的话则会更新出售表中状态
                return b ? Result.success("更新出售状态成功!") : Result.error("更新出售状态失败!");
            }
            return Result.error("什么都没执行");
        } finally {
            // 3.更新缓存数据
            //1）更新redis和es中数据 // 这里实际上可以直接将缓存给删掉，等下次查询的时候自动更新最新的，无需修改
            String reidsKey = Constants.RedisKey.REDIS_COLLECTION(updataCollectionReq.getId());
            redisUtil.del(reidsKey);
            //释放写锁
            distributedRedisLock.releaseWriteLock(Constants.RedisKey.READ_WRITE_LOCK(updataCollectionReq.getId()));
        }
    }
}
