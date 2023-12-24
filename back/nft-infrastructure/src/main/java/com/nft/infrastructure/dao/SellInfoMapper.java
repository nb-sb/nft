package com.nft.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.infrastructure.po.SellInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author NBSB
 * @since 2023-12-08
 */
public interface SellInfoMapper extends BaseMapper<SellInfo> {

    @Select("SELECT sell_info.id, sell_info.`hash`, sell_info.ipfs_hash, sell_info.amount, sell_info.remain, sell_info.auther, sell_info.`status`, " +
            "submit_cache.path, submit_cache.present, submit_cache.`name`, submit_cache.price " +
            "FROM sell_info " +
            "JOIN submit_cache ON sell_info.unique_id = submit_cache.id " +
            "ORDER BY sell_info.id DESC ")
    IPage<ConllectionInfoVo> selectConllectionByPage(Page<ConllectionInfoVo> page);

    //使用分类查询
    @Select("SELECT sell_info.id, sell_info.`hash`, sell_info.ipfs_hash, sell_info.amount, sell_info.remain, sell_info.auther, sell_info.`status`, submit_cache.path, submit_cache.present, " +
            "submit_cache.`name`, submit_cache.price, nft_relationships.mid " +
            "FROM sell_info " +
            "JOIN submit_cache ON sell_info.unique_id = submit_cache.id " +
            "JOIN nft_relationships ON sell_info.id = nft_relationships.cid " +
            "WHERE nft_relationships.mid = #{mid} " +
            "ORDER BY sell_info.id DESC")
    IPage<ConllectionInfoVo> selectConllectionKindByPage(Page<ConllectionInfoVo> page, @Param("mid") Long mid);

}
