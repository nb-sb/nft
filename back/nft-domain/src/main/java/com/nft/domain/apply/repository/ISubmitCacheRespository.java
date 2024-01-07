package com.nft.domain.apply.repository;

import com.nft.domain.apply.model.vo.SubCacheVo;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.user.model.vo.UserVo;

public interface ISubmitCacheRespository {
    SubCacheVo selectOneByHash(String hash);
    boolean addSellCheck(ApplyReq applyReq, UserVo userVo);
    boolean upDateSubStatus(ReviewReq req);//更新提交状态
    boolean updataSellStatus(UpdataCollectionReq updataCollectionReq);//更新出售状态
    SubCacheVo selectSubSellById(Integer id);
}
