package com.nft.domain.nft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;

import java.util.List;

public interface INftSelectService {
    //查询藏品信息
    ConllectionInfoVo selectConllectionById(Integer id);

    //分页查询藏品
    List<ConllectionInfoVo> selectConllectionByPage(Page<ConllectionInfoVo> page);

    //分页 按照藏品分类查询藏品
    List<ConllectionInfoVo> selectConllectionKindByPage(Page<ConllectionInfoVo> page,Integer mid);
}
