package com.nft.domain.apply.service;

import com.nft.common.Result;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.user.model.vo.UserVo;

public interface INftSubmitService {
    //添加到审核列表中
    Result creat(UserVo fromUser, ApplyReq applyReq);

    //审核藏品
    Result ReviewCollection(ReviewReq req);

    //查询待审核藏品

    //管理员 查询全部藏品申请by page
    Result selectAllApplyPage();
    //查询自己的所属提交

}
