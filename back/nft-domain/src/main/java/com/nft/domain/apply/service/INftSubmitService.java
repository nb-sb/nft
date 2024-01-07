package com.nft.domain.apply.service;

import com.nft.common.Result;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.nft.model.res.AuditRes;

import javax.servlet.http.HttpServletRequest;

public interface INftSubmitService {
    //添加到审核列表中
    Result addApply(HttpServletRequest httpServletRequest, ApplyReq applyReq);

    //修改藏品审核状态
    AuditRes changeSellStatus(ReviewReq req);

    boolean insertSellInfo(ReviewReq req, String hash);
    //审核藏品
    AuditRes ReviewCollection(ReviewReq req);

    //查询待审核藏品

    //管理员 查询全部藏品信息by page

    //查询自己的所属提交

}
