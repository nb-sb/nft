package com.nft.domain.nft.service.impl;

import com.nft.common.Redis.RedisUtil;
import com.nft.domain.nft.repository.INftSellRespository;
import com.nft.domain.nft.repository.IOrderInfoRespository;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.domain.nft.service.INftTransferService;
import com.nft.domain.support.ipfs.IpfsService;
import com.nft.domain.user.repository.IUserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class NftTransferService implements INftTransferService {
    private final INftSellRespository iNftSellRespository;
    private final RedisUtil redisUtil;
    private final IpfsService ipfsService;
    private final IUserInfoRepository iUserInfoRepository;
    private final IOrderInfoRespository iOrderInfoRespository;
    private final IOwnerShipRespository iOwnerShipRespository;
    @Override
    public void transferCollection() {

    }
}
