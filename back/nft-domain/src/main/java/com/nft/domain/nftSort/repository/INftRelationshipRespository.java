package com.nft.domain.nftSort.repository;

import com.nft.domain.nftSort.model.entity.MetaRelationShipEntity;

/**
* @author: 戏人看戏
* @Date: 2024/1/7 16:31
* @Description: 藏品信息关联的存贮操作
*/
public interface INftRelationshipRespository {

    //添加分类表
    boolean creat(MetaRelationShipEntity metaRelationShipEntity);

    Integer loadMid(Integer cid);
}
