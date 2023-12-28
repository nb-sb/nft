package com.nft.domain.nft.repository;

import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.OrderInfoVo;

public interface IOrderInfoRespository {

    //添加订单表
    boolean addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid);

    OrderInfoVo selectOrderInfoByNumber(String orderNumber);

    //设置订单支付状态
    boolean setPayOrderStatus(String orderNumber, Integer status);

    boolean setOrderStatus(String orderNumber, Integer status);
}
