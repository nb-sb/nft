package com.nft.domain.nft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Result;
import com.nft.domain.nft.model.res.GetNftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;

import java.util.List;

public interface INftSelectService {
    //查询藏品信息
    GetNftRes selectConllectionById(Integer id);

    //分页查询藏品
    GetNftRes selectConllectionByPage(Page<ConllectionInfoVo> page);

    //分页 按照藏品分类查询藏品
    GetNftRes selectConllectionKindByPage(Page<ConllectionInfoVo> page,Integer mid);
}
