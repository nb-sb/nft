package com.nft.domain.nft.service;

import com.nft.domain.nft.model.req.TransferReq;

import javax.servlet.http.HttpServletRequest;

public interface INftTransferService {
    //转移藏品方法
    void transferCollection(TransferReq transferReq, HttpServletRequest httpServletRequest);
}
