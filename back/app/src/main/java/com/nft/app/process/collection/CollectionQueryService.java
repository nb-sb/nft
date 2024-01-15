package com.nft.app.process.collection;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Constants;
import com.nft.common.PageRequest;
import com.nft.common.Redis.RedisConstant;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.common.Result;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.nft.model.req.InfoKindReq;
import com.nft.domain.nft.model.res.GetNftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.repository.ISellInfoRespository;
import lombok.AllArgsConstructor;
import org.fisco.bcos.sdk.utils.StringUtils;
import org.springframework.stereotype.Service;

import static com.nft.common.Redis.RedisConstant.DAY_ONE;
import static com.nft.common.Redis.RedisConstant.MINUTE_3;

@Service
@AllArgsConstructor
public class CollectionQueryService {
    private final RedisUtil redisUtil;
    private final ISellInfoRespository iSellInfoRespository;
    private final DistributedRedisLock distributedRedisLock;
    private final ISubmitCacheRespository iSubmitCacheRespository;
    public ConllectionInfoVo getConllectionCache(String reidKey) {
        String conllectionCacheKey = redisUtil.getStr(reidKey);
        if (!StringUtils.isEmpty(conllectionCacheKey)) {
            //如果是空缓存的话 //这种原因一般是藏品已经删除，但是还有大量数据查询的情况,直接返回一个空对象就可以了
            if (Constants.RedisKey.REDIS_EMPTY_CACHE().equals(conllectionCacheKey)) {
                System.out.println("如果是空缓存的话 //这种原因一般是藏品已经删除，但是还有大量数据查询的情况,直接返回一个空对象就可以了");
                return new ConllectionInfoVo();
            }
            return JSONUtil.toBean(conllectionCacheKey, ConllectionInfoVo.class);
        }
        return null;
    }
    //查询藏品信息
    public GetNftRes selectConllectionById(Integer id) {
        String reidsKey = Constants.RedisKey.REDIS_COLLECTION(id);
        ConllectionInfoVo conllectionInfoVo ;
        //第一层开始查询缓存，若查不到才开始查询mysql
        conllectionInfoVo = iSellInfoRespository.selectCacheByCollectId(id);
        if (conllectionInfoVo != null) {
            return GetNftRes.success( conllectionInfoVo);
        }
        //三、突发性热点问题DCL（冷门商品爆单,大量请求穿过第一层到第二层开始打到数据上） -->优化锁 ：此锁优化方案 1.串行转并发 2.jvm缓存，实际上无需优化，因为大多数都在第一阶段进行返回内容了
        distributedRedisLock.acquire(Constants.RedisKey.LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX(id));
        try {
            //1.在加一层查询缓存，防止所有请求全部打到数据库中，并且有分布式锁的加持，实际上只会查到次数据库就把数据更新到redis中了，优化很大性能
            conllectionInfoVo = iSellInfoRespository.selectCacheByCollectId(id);
            if (conllectionInfoVo != null) {
                return GetNftRes.success( conllectionInfoVo);
            }
            //四、缓存与数据库双写不一致问题
            //1.这里设置读锁,在查询mysql之前设置读锁,然后在updata方法里设置更新锁，这个方法比延时双删要好
            distributedRedisLock.acquireReadLock(Constants.RedisKey.READ_WRITE_LOCK(id));
            try {
                conllectionInfoVo = iSellInfoRespository.selectByCollectId(id);
                if (conllectionInfoVo != null) {
                    //一、设置缓存并解决 缓存击穿问题
                    //查询到的mysql中数据添加到redis时 添加24h + random s的redis缓存，随机增加几百秒缓存
                    redisUtil.set(reidsKey, JSONUtil.toJsonStr(conllectionInfoVo), RedisConstant.RandomCacheTime(DAY_ONE));
                } else {
                    //二、缓存穿透问题(商品已经删了，redis中无数据，大量请求打到数据库中导致数据库宕机)
                    //添加几分钟 redis空缓存
                    redisUtil.set(reidsKey, Constants.RedisKey.REDIS_EMPTY_CACHE(), RedisConstant.RandomCacheTime(MINUTE_3));
                }
                return GetNftRes.success(conllectionInfoVo);
            } finally {
                distributedRedisLock.releaseReadLock(Constants.RedisKey.READ_WRITE_LOCK(id));
            }
        } finally {
            distributedRedisLock.release(Constants.RedisKey.LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX(id));
        }
    }


    //按照分类查询出售藏品
    public GetNftRes selectSellConllectionKindPage(InfoKindReq infoKindReq) {
        return GetNftRes.success(iSellInfoRespository.selectSellConllectionKindByPage(new Page<>(infoKindReq.getCurrent(), infoKindReq.getPageSize())
                , infoKindReq.getMin()).getRecords()
        );
    }

}

