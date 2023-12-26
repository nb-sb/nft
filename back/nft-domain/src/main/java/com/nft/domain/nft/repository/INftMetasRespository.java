package com.nft.domain.nft.repository;

public interface INftMetasRespository {

    //修改分类数量
    boolean incr(Integer mid, Integer amount);

    boolean isExist(Integer mid);



}
