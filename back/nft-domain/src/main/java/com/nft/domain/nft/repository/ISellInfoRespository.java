package com.nft.domain.nft.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.entity.SellInfoEntity;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;

import java.math.BigInteger;

/**
* @author: 戏人看戏
* @Date: 2024/1/7 16:19
* @Description:  sell_info 在售藏品的的操作
*/
public interface ISellInfoRespository {
    //添加出售记录
    boolean creat(SellInfoEntity sellInfoEntity);
    //添加出售记录
    boolean addSellByFISCO(String hash, BigInteger totail);



    ConllectionInfoVo selectCacheByCollectId(Integer id);
    SellInfoEntity selectSellInfoById(Integer id);
    //查询出售中的藏品
    IPage<ConllectionInfoVo> selectSellCollectionByPage(Page page);
    //查询指定分类中正在出售的藏品
    IPage<ConllectionInfoVo> selectSellConllectionKindByPage(Page page, Integer mid);

    boolean updataConllectionInfo(UpdataCollectionReq updataCollectionReq);
    //设置库存
    boolean setSellStocks(Integer id, Integer number);
}
