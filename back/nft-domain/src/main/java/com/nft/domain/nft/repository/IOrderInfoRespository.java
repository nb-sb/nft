package com.nft.domain.nft.repository;

import com.nft.domain.nft.model.vo.ConllectionInfoVo;

public interface IOrderInfoRespository {

    //添加订单表
    void addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid);
}
