package com.nft.app.process.sort;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Constants;
import com.nft.common.PageRequest;
import com.nft.common.Redis.RedisConstant;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import com.nft.domain.nftSort.model.entity.MetaEntity;
import com.nft.domain.nftSort.model.res.SortRes;
import com.nft.domain.nftSort.repository.ISortRepository;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class SortQueryService {
    private final ISortRepository iSortRepository;
    private final RedisUtil redisUtil;
    public Result pageList(PageRequest query) {
        //几乎不变的分类，可以使用缓存存贮减轻数据库压力
        String sortPage = String.valueOf(redisUtil.get(Constants.RedisKey.SortPage));
        List<MetaEntity> sortVos = null;
        try {
            if (sortPage != null) {
                sortVos = (List<MetaEntity>) JSONUtil.parse(sortPage);
                return SortRes.success(sortVos);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        synchronized (SortQueryService.class) {
            //抢到锁之后再次判断是否为空
            Object obj = redisUtil.get(Constants.RedisKey.SortPage);
            if(obj == null) {
                sortVos = iSortRepository.selectSortByPage(
                        new Page<>(query.getCurrent(), query.getPageSize())
                );
                redisUtil.set(Constants.RedisKey.SortPage, JSONUtil.toJsonStr(sortVos), RedisConstant.DAY_ONE);
            }
        }
        return SortRes.success(sortVos);
    }
}
