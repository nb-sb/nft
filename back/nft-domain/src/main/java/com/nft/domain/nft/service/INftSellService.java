package com.nft.domain.nft.service;

import com.nft.common.Result;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;

import javax.servlet.http.HttpServletRequest;

public interface INftSellService {

    //添加到审核列表中
    NftRes addSellCheck(HttpServletRequest httpServletRequest, SellReq sellReq);

    //审核藏品，修改藏品状态
    AuditRes changeSellStatus(ReviewReq req);


    boolean insertSellInfo(ReviewReq req, String hash);



    //购买藏品
    Result purchaseConllection(HttpServletRequest httpServletRequest,Integer ConllectionID);

    //支付订单
    void payOrder();



    //修改商品信息 -1.可以在用户购买商品的时候用于更新剩余信息等 2.管理员更新藏品信息的时候用
    Result updataConllectionInfo(UpdataCollectionReq updataCollectionReq);

    AuditRes ReviewCollection(ReviewReq req);

}
