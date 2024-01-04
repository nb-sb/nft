package com.nft.domain.nft.service;

import com.nft.common.Result;
import com.nft.domain.nft.model.req.TransferReq;

import javax.servlet.http.HttpServletRequest;

public interface INftTransferService {
    //转移藏品方法
    Result transferCollection(TransferReq transferReq, HttpServletRequest httpServletRequest);
}
