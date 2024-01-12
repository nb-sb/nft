package com.nft.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.infrastructure.po.SellInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
public interface SellInfoMapper extends BaseMapper<SellInfo> {

    @Select("SELECT nft_sell_info.id, nft_sell_info.`hash`, nft_sell_info.ipfs_hash, nft_sell_info.amount, nft_sell_info.remain, nft_sell_info.auther, nft_sell_info.`status`, " +
            "nft_submit_cache.path, nft_submit_cache.present, nft_submit_cache.`name`, nft_submit_cache.price " +
            "FROM nft_sell_info " +
            "JOIN nft_submit_cache ON nft_sell_info.unique_id = nft_submit_cache.id " +
            "ORDER BY nft_sell_info.id DESC ")
    IPage<ConllectionInfoVo> selectConllectionByPage(Page<ConllectionInfoVo> page);

    //使用分类查询
    @Select("SELECT nft_sell_info.id, nft_sell_info.`hash`, nft_sell_info.ipfs_hash, nft_sell_info.amount, nft_sell_info.remain, nft_sell_info.auther, nft_sell_info.`status`, nft_submit_cache.path, nft_submit_cache.present, " +
            "nft_submit_cache.`name`, nft_submit_cache.price, nft_relationships.mid " +
            "FROM nft_sell_info " +
            "JOIN nft_submit_cache ON nft_sell_info.unique_id = nft_submit_cache.id " +
            "JOIN nft_relationships ON nft_sell_info.id = nft_relationships.cid " +
            "WHERE nft_relationships.mid = #{mid} " +
            "ORDER BY nft_sell_info.id DESC")
    IPage<ConllectionInfoVo> selectSellConllectionKindByPage(Page<ConllectionInfoVo> page, @Param("mid") Long mid);

}
