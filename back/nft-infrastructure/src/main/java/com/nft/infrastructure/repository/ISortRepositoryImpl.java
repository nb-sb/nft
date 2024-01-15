package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nftSort.model.entity.MetaEntity;
import com.nft.domain.nftSort.repository.ISortRepository;
import com.nft.infrastructure.dao.NftMetasMapper;
import com.nft.infrastructure.po.NftMetas;
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
    public boolean creat(MetaEntity metaEntity) {
        NftMetas nftMetas = new NftMetas();
        nftMetas.setName(metaEntity.getName())
                .setSlug(metaEntity.getSlug())
                .setCount(metaEntity.getCount());
        int insert = nftMetasMapper.insert(nftMetas);
        return insert>0;
    }

    @Override
    public boolean updateSort(MetaEntity entity) {
        NftMetas nftMetas = new NftMetas();
        nftMetas.setMid(entity.getMid());
        nftMetas.setName(entity.getName());
        nftMetas.setSlug(entity.getSlug());
        nftMetas.setCount(entity.getCount());
        int update = nftMetasMapper.updateById(nftMetas);
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
    public List<MetaEntity> selectSortByPage(Page page1) {
        Page<NftMetas> page = new Page<>(page1.getCurrent(), page1.getSize());
        page.setOptimizeCountSql(true);
        Page<NftMetas> userInfoPage = nftMetasMapper.selectPage(page, null);
        List<NftMetas> records = userInfoPage.getRecords();
        if (records.size()==0) return null;
        List<MetaEntity> userVos = BeanCopyUtils.convertListTo(records, MetaEntity::new);
        return userVos;
    }

    @Override
    public MetaEntity selectSortByName(String name,String slug) {
        QueryWrapper<NftMetas> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name)
                .or().eq("slug", slug);
        NftMetas nftMetas = nftMetasMapper.selectOne(queryWrapper);
        if (nftMetas == null) return null;
        return BeanCopyUtils.convertTo(nftMetas, MetaEntity::new);
    }

    @Override
    public MetaEntity selectSortByMid(Integer mid) {
        LambdaQueryWrapper<NftMetas> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NftMetas::getMid, mid).select();
        NftMetas nftMetas = nftMetasMapper.selectOne(queryWrapper);
        if (nftMetas == null) return null;
        MetaEntity meta = new MetaEntity();
        meta.setMid(nftMetas.getMid());
        meta.setName(nftMetas.getName());
        meta.setSlug(nftMetas.getSlug());
        meta.setCount(nftMetas.getCount());
        return meta;
    }
}
