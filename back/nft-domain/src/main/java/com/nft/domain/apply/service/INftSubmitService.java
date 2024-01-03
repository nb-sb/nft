package com.nft.domain.apply.service;

import com.nft.common.Result;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.res.AuditRes;

import javax.servlet.http.HttpServletRequest;

public interface INftSubmitService {
    //添加到审核列表中
    Result addSellCheck(HttpServletRequest httpServletRequest, SellReq sellReq);

    //审核藏品，修改藏品状态
    AuditRes changeSellStatus(ReviewReq req);

    boolean insertSellInfo(ReviewReq req, String hash);
}
