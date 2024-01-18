package com.nft.domain.apply.service;

import com.nft.common.Result;
import com.nft.domain.apply.model.entity.SubmitSellEntity;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.user.model.entity.UserEntity;

public interface INftSubmitService {
    //添加到审核列表中
    Result creat(UserEntity fromUser, ApplyReq applyReq);


    boolean insertSellInfo(SubmitSellEntity submitSellEntity, String ipfshash);

    //查询自己的所属提交

}
