package com.nft.domain.nft.repository;
/**
* @author: 戏人看戏
* @Date: 2024/1/7 16:31
* @Description: 藏品信息关联的存贮操作
*/
public interface INftRelationshipRespository {

    //添加分类表
    boolean addMetas( Integer cid , Integer mid);
}
