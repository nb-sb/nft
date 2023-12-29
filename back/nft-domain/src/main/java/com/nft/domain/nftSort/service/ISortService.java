package com.nft.domain.nftSort.service;

import com.nft.domain.nftSort.model.req.SortReq;
import com.nft.domain.nftSort.model.req.UpdateSortReq;
import com.nft.domain.nftSort.model.vo.SortVo;
import com.nft.domain.support.Search;

import java.util.List;

public interface ISortService {
    //新增分类
    boolean addSort(SortReq sortReq);

    //删除分类
    boolean delSortById(Integer id);

    //查询分类
    List<SortVo> selectSortByPage(Search search);

    //修改分类信息
    boolean updateCollection(UpdateSortReq updateSortReq);
}
