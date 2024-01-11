package com.nft.domain.nft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.res.GetNftRes;

public interface INftInfoService {
    //查询藏品信息
    GetNftRes selectConllectionById(Integer id);

    //分页查询藏品
    GetNftRes selectSellConllectionPage(Page page);

    //分页 按照藏品分类查询藏品
    GetNftRes selectSellConllectionKindPage(Page page, Integer mid);
}
