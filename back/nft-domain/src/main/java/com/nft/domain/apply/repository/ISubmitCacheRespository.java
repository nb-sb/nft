package com.nft.domain.apply.repository;

import com.nft.domain.apply.model.vo.SubCacheVo;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.user.model.vo.UserVo;

public interface ISubmitCacheRespository {
    SubCacheVo selectOneByHash(String hash);
    boolean addSellCheck(ApplyReq applyReq, UserVo userVo);
    /**
     * @Des 更新提交数据的状态
     * @Date 2024/1/9 17:04
     * @Param id 提交表id
     * @Param status 修改的状态
     * @Return
     */
    boolean upDateSubStatus(Integer id,Integer status);//更新提交状态
    boolean updataSellStatus(UpdataCollectionReq updataCollectionReq);//更新出售状态
    SubCacheVo selectById(Integer id);
}
