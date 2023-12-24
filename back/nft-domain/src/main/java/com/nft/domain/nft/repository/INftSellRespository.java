package com.nft.domain.nft.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.SubCacheVo;

import java.util.Map;

public interface INftSellRespository {
    NftRes addSellCheck(SellReq sellReq, Map<String, String> userMap);

    boolean upDateSubStatus(ReviewReq req);//更新提交状态

    boolean updataSellStatus(UpdataCollectionReq updataCollectionReq);//更新出售状态


    boolean insertSellInfo(Integer id, String hash);

    boolean addSellByFISCO(String hash,Integer id);


    SubCacheVo selectSubSellById(Integer id);

    ConllectionInfoVo selectConllectionById(Integer id);

    IPage<ConllectionInfoVo> selectConllectionByPage(Page<ConllectionInfoVo> page);

    IPage<ConllectionInfoVo> selectConllectionKindByPage(Page<ConllectionInfoVo> page, Integer mid);

    boolean updataConllectionInfo(UpdataCollectionReq updataCollectionReq);
}
