package com.nft.domain.nft.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.SellInfoVo;
import com.nft.domain.apply.model.vo.SubCacheVo;
import com.nft.domain.user.model.vo.UserVo;

public interface INftSellRespository {


    boolean insertSellInfo(Integer id, String hash);

    boolean addSellByFISCO(String hash,Integer id);




    ConllectionInfoVo selectConllectionById(Integer id);
    SellInfoVo selectSellInfoById(Integer id);

    IPage<ConllectionInfoVo> selectConllectionByPage(Page<ConllectionInfoVo> page);

    IPage<ConllectionInfoVo> selectConllectionKindByPage(Page<ConllectionInfoVo> page, Integer mid);

    boolean updataConllectionInfo(UpdataCollectionReq updataCollectionReq);

    //减少库存
    boolean decreaseSellStocks(Integer id,Integer number);





}
