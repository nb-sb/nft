package com.nft.app.process.sort;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.PageRequest;
import com.nft.common.Result;
import com.nft.domain.nftSort.model.entity.MetaEntity;
import com.nft.domain.nftSort.model.res.SortRes;
import com.nft.domain.nftSort.repository.ISortRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class SortQueryService {
    private final ISortRepository iSortRepository;
    public Result pageList(PageRequest query) {
        // TODO: 2024/1/15 几乎不变的分类，可以使用缓存存贮减轻数据库压力
        List<MetaEntity> sortVos = iSortRepository.selectSortByPage(
                new Page<>(query.getCurrent(), query.getPageSize())
        );
        return SortRes.success(sortVos);
    }
}
