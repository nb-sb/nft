package com.nft.domain.nftSort.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nftSort.model.entity.MetaEntity;

import java.util.List;

public interface ISortRepository {
    //添加分类
    boolean creat(MetaEntity metaEntity);
    //修改分类
    boolean updateSort(MetaEntity entity);

    boolean delSortById(Integer id);

    List<MetaEntity> selectSortByPage(Page page);

    MetaEntity selectSortByName(String name,String slug);

    MetaEntity selectSortByMid(Integer mid);
    //查询分类
    //删除分类

    boolean isExist(Integer mid);
}
