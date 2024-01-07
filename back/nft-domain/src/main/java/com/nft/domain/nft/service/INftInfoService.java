package com.nft.domain.nft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.res.GetNftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;

public interface INftInfoService {
    //查询藏品信息
    GetNftRes selectConllectionById(Integer id);

    //分页查询藏品
    GetNftRes selectSellConllectionByPage(Page page);

    //分页 按照藏品分类查询藏品
    GetNftRes selectSellConllectionKindByPage(Page page, Integer mid);
}
