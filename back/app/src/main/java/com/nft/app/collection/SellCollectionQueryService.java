package com.nft.app.collection;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.PageRequest;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Redission.DistributedRedisLock;
import com.nft.domain.nft.model.res.GetNftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.repository.ISellInfoRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SellCollectionQueryService {
    private final RedisUtil redisUtil;
    private final ISellInfoRespository iSellInfoRespository;
    private final DistributedRedisLock distributedRedisLock;
    public GetNftRes pageList(PageRequest pageRequest) {
        Page<Object> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
        IPage<ConllectionInfoVo> conllectionInfoVoIPage = iSellInfoRespository.selectSellCollectionByPage(page);
        return GetNftRes.success(conllectionInfoVoIPage.getRecords());
    }
}
