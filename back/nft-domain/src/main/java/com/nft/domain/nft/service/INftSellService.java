package com.nft.domain.nft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Result;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface INftSellService {

    //添加到审核列表中
    NftRes addSellCheck(HttpServletRequest httpServletRequest, SellReq sellReq);

    //审核藏品，修改藏品状态
    AuditRes changeSellStatus(ReviewReq req);


    boolean insertSellInfo(ReviewReq req, String hash);

    //将藏品添加至区块链中
    /**
     * @Des 添加出售数据至区块链中
     * @Date 2023/12/13 19:32
     * @Param hash 是实际的ipfs hash， id为提交的id
     * @Return bolean
     */
    boolean addSellByFISCO(String hash,Integer id);

    //购买藏品
    void purchaseConllection();




    //修改商品信息 -1.可以在用户购买商品的时候用于更新剩余信息等 2.管理员更新藏品信息的时候用
    Result updataConllectionInfo(UpdataCollectionReq updataCollectionReq);

    AuditRes ReviewCollection(ReviewReq req);

}
