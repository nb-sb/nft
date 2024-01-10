package com.nft.domain.nft.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.domain.nft.model.res.GetNftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.domain.nft.service.INftInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fisco.bcos.sdk.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@AllArgsConstructor
@Service
@Log4j2
public class NftInfoService implements INftInfoService {
    private  final RedisUtil redisUtil;
    private final ISellInfoRespository iSellInfoRespository;
    private final DistributedRedisLock distributedRedisLock;
//    private final ElasticsearchRestTemplate elasticTemplate ;
//    @PostConstruct
//    public void init() {
//        //获取索引对象
//        IndexOperations indexOperations = elasticTemplate.indexOps(ConllectionInfoVo.class);
//        //判断是否存贮索引
//        boolean exists = indexOperations.exists();
//        if (!exists) {
//            //根据这个class对象创建索引
//            try {
//                indexOperations.create();
//                System.out.println("构建索引成功");
//                //获取映射
//                Document mapping = indexOperations.createMapping(ConllectionInfoVo.class);
//                //将映射放入索引
//                indexOperations.putMapping(mapping);
//            } catch (Exception e) {
//                System.err.println(e);
//            }
//        }
//    }
    @Override
    public GetNftRes selectSellConllectionByPage(Page page) {
        IPage<ConllectionInfoVo> conllectionInfoVoIPage = iSellInfoRespository.selectSellConllectionByPage(page);
        return GetNftRes.success(conllectionInfoVoIPage.getRecords());
    }

    @Override
    public GetNftRes selectSellConllectionKindByPage(Page page, Integer mid) {
        return GetNftRes.success(iSellInfoRespository.selectSellConllectionKindByPage(page, mid).getRecords());
    }

    public ConllectionInfoVo getConllectionCache(String reidKey) {
        try {
            String conllectionCacheKey = redisUtil.getStr(reidKey);
            if (!StringUtils.isEmpty(conllectionCacheKey)) {
                //如果是空缓存的话 //这种原因一般是藏品已经删除，但是还有大量数据查询的情况,直接返回一个空对象就可以了
                if (Constants.RedisKey.REDIS_EMPTY_CACHE().equals(conllectionCacheKey)) {
                    return new ConllectionInfoVo();
                }
                return JSONUtil.toBean(conllectionCacheKey, ConllectionInfoVo.class);
            }
            return null;
        } catch (Exception e) {
            log.error("redis 连接错误：" + e.getMessage());
            return null;
        }
    }
    //查询藏品信息
    @Override
    public GetNftRes selectConllectionById(Integer id) {
        String reidsKey = Constants.RedisKey.REDIS_COLLECTION(id);
        ConllectionInfoVo conllectionInfoVo ;
        //第一层开始查询缓存，若查不到才开始查询mysql
        conllectionInfoVo = getConllectionCache(reidsKey);
        if (conllectionInfoVo != null) {
            return GetNftRes.success( conllectionInfoVo);
        }
        //三、突发性热点问题DCL（冷门商品爆单,大量请求穿过第一层到第二层开始打到数据上） -->优化锁 ：此锁优化方案 1.串行转并发 2.jvm缓存，实际上无需优化，因为大多数都在第一阶段进行返回内容了
        distributedRedisLock.acquire(Constants.RedisKey.LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX(id));
        try {
            //1.在加一层查询缓存，防止所有请求全部打到数据库中，并且有分布式锁的加持，实际上只会查到次数据库就把数据更新到redis中了，优化很大性能
            conllectionInfoVo = getConllectionCache(reidsKey);
            if (conllectionInfoVo != null) {
                return GetNftRes.success( conllectionInfoVo);
            }
            //四、缓存与数据库双写不一致问题
            //1.这里设置读锁,在查询mysql之前设置读锁,然后在updata方法里设置更新锁，这个方法比延时双删要好
            distributedRedisLock.acquireReadLock(Constants.RedisKey.READ_WRITE_LOCK(id));
            try {
                conllectionInfoVo = iSellInfoRespository.selectConllectionById(id);
                Random rand = new Random();
                if (conllectionInfoVo != null) {
                    //一、设置缓存并解决 缓存击穿问题
                    //查询到的mysql中数据添加到redis时 添加24h + random s的redis缓存，随机增加几百秒缓存
                    redisUtil.set(reidsKey, JSONUtil.toJsonStr(conllectionInfoVo), 60 * 60 * 24 + rand.nextInt(900) + 100);
                } else {
                    //二、缓存穿透问题(商品已经删了，redis中无数据，大量请求打到数据库中导致数据库宕机)
                    //添加redis空缓存
                    redisUtil.set(reidsKey, Constants.RedisKey.REDIS_EMPTY_CACHE(), 60 * 3 + rand.nextInt(900) + 100);
                }
                return GetNftRes.success( conllectionInfoVo);
            } finally {
                distributedRedisLock.releaseReadLock(Constants.RedisKey.READ_WRITE_LOCK(id));
            }
        } finally {
            distributedRedisLock.release(Constants.RedisKey.LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX(id));
        }
    }
}
