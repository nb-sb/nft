package com.nft.app.process.collection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.PageRequest;
import com.nft.common.Result;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.nft.model.res.GetNftRes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CollectionQueryService {
    private final ISubmitCacheRespository iSubmitCacheRespository;

    public Result applyPageList(PageRequest pageRequest) {
        //管理员查询申请的数据
        return GetNftRes.success(iSubmitCacheRespository.selectApplyByPage(new Page<>(pageRequest.getCurrent(),
                pageRequest.getPageSize())));
    }


}

