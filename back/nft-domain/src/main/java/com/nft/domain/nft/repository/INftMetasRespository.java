package com.nft.domain.nft.repository;
/**
* @author: 戏人看戏
* @Date: 2024/1/7 16:32
* @Description: 藏品分类存贮操作
*/
public interface INftMetasRespository {

    //修改分类数量
    boolean incr(Integer mid, Integer amount);

    boolean isExist(Integer mid);



}
