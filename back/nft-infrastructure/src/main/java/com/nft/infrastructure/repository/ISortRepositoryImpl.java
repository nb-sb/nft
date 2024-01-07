package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nftSort.model.req.SortReq;
import com.nft.domain.nftSort.model.req.UpdateSortReq;
import com.nft.domain.nftSort.model.vo.SortVo;
import com.nft.domain.nftSort.repository.ISortRepository;
import com.nft.domain.support.Search;
import com.nft.infrastructure.dao.NftMetasMapper;
import com.nft.infrastructure.po.NftMetas;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Log4j2
@AllArgsConstructor
public class ISortRepositoryImpl implements ISortRepository {
    private final NftMetasMapper nftMetasMapper;


    @Override
    public boolean addSort(SortReq sortReq) {
        NftMetas nftMetas = BeanCopyUtils.convertTo(sortReq, NftMetas ::new);
        nftMetas.setCount(0);
        int insert = nftMetasMapper.insert(nftMetas);
        return insert>0;
    }

    @Override
    public boolean updateSort(UpdateSortReq updateSortReq) {
        NftMetas nftMetas = BeanCopyUtils.convertTo(updateSortReq, NftMetas::new);
        UpdateWrapper<NftMetas> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("mid", updateSortReq.getMid());
        int update = nftMetasMapper.update(nftMetas, updateWrapper);
        return update>0;
    }

    @Override
    public boolean delSortById(Integer id) {
        QueryWrapper<NftMetas> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mid", id);
        int delete = nftMetasMapper.delete(queryWrapper);
        return delete > 0;
    }

    @Override
    public List<SortVo> selectSortByPage(Search search) {
        Integer current = Math.toIntExact(search.getCurrent());
        Integer size = Math.toIntExact(search.getPageSize());
        Page<NftMetas> page = new Page<>(current, size);
        page.setOptimizeCountSql(true);
        Page<NftMetas> userInfoPage = nftMetasMapper.selectPage(page, null);
        List<NftMetas> records = userInfoPage.getRecords();
        List<SortVo> userVos = BeanCopyUtils.convertListTo(records, SortVo::new);
        return userVos;
    }

    @Override
    public SortVo selectSortByName(SortReq sortReq) {
        QueryWrapper<NftMetas> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conllection_name", sortReq.getConllectionName())
                .or().eq("conllection_slug", sortReq.getConllectionSlug());
        NftMetas nftMetas = nftMetasMapper.selectOne(queryWrapper);
        return BeanCopyUtils.convertTo(nftMetas, SortVo::new);
    }
}
