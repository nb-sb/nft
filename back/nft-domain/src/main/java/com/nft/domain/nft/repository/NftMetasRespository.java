package com.nft.domain.nft.repository;

public interface NftMetasRespository {

    //修改分类数量
    boolean incr(Integer mid, Integer amount);

    boolean isExist(Integer mid);



}
