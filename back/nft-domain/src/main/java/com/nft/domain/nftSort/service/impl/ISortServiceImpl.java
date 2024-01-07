package com.nft.domain.nftSort.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nftSort.model.req.SortReq;
import com.nft.domain.nftSort.model.req.UpdateSortReq;
import com.nft.domain.nftSort.model.vo.SortVo;
import com.nft.domain.nftSort.repository.ISortRepository;
import com.nft.domain.nftSort.service.ISortService;
import com.nft.domain.support.Search;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ISortServiceImpl implements ISortService {
    private final ISortRepository iSortRepository;
    @Override
    public boolean addSort(SortReq sortReq) {
        SortVo sortVo = iSortRepository.selectSortByName(sortReq);
        if (sortVo != null) return false;
        return iSortRepository.addSort(sortReq);
    }

    @Override
    public boolean delSortById(Integer id) {
        return iSortRepository.delSortById(id);
    }

    @Override
    public List<SortVo> selectSortByPage(Page page) {
        return iSortRepository.selectSortByPage(page);
    }

    @Override
    public boolean updateCollection(UpdateSortReq updateSortReq) {
        return iSortRepository.updateSort(updateSortReq);
    }

}
