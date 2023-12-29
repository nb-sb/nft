package com.nft.domain.nftSort.repository;

import com.nft.domain.nftSort.model.req.SortReq;
import com.nft.domain.nftSort.model.req.UpdateSortReq;
import com.nft.domain.nftSort.model.vo.SortVo;
import com.nft.domain.support.Search;

import java.util.List;

public interface ISortRepository {
    //添加分类
    boolean addSort(SortReq sortReq);
    //修改分类
    boolean updateSort(UpdateSortReq updateSortReq);

    boolean delSortById(Integer id);

    List<SortVo> selectSortByPage(Search search);

    SortVo selectSortByName(SortReq sortReq);
    //查询分类
    //删除分类

}
