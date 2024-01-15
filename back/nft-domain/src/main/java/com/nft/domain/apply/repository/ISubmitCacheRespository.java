package com.nft.domain.apply.repository;

import com.nft.domain.apply.model.entity.SubmitSellEntity;
import com.nft.domain.nft.model.req.UpdataCollectionReq;

public interface ISubmitCacheRespository {
    boolean creat(SubmitSellEntity submitSellEntity);
    Integer selectIdByHash(String hash);


    SubmitSellEntity selectOneByHash(String hash);
    /**
     * @Des 更新提交数据的状态
     * @Date 2024/1/9 17:04
     * @Param id 提交表id
     * @Param status 修改的状态
     * @Return
     */
    boolean upDateSubStatus(SubmitSellEntity submitSellEntity);//更新提交状态
    boolean updataSellStatus(UpdataCollectionReq updataCollectionReq);//更新出售状态
    SubmitSellEntity selectById(Integer id);
}
