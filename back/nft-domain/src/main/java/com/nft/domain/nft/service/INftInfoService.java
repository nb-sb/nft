package com.nft.domain.nft.service;

import com.nft.domain.nft.model.vo.ConllectionInfoVo;

public interface INftInfoService {
    //查询正在出售的藏品 详细信息
    ConllectionInfoVo selectByCollectId(Integer id);
}
