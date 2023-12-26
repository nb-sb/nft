package com.nft.domain.nft.repository;

import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.OrderInfoVo;

public interface IOrderInfoRespository {

    //添加订单表
    void addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid);

    OrderInfoVo selectOrderInfoByNumber(String orderNumber);
}
