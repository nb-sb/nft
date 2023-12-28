package com.nft.domain.nft.repository;

import cn.hutool.db.sql.Order;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.OrderInfoVo;

import java.text.ParseException;
import java.util.List;

public interface IOrderInfoRespository {

    //添加订单表
    boolean addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid);

    OrderInfoVo selectOrderInfoByNumber(String orderNumber);

    //查询用户指定商品订单
    List<OrderInfoVo> selectOrderInfoByUser(Integer userId, Integer collectionId, Integer OrderStatus);

    //todo查询用户拥有的商品订单列表


    //设置订单支付状态
    boolean setPayOrderStatus(String orderNumber, Integer status);

    boolean setOrderStatus(String orderNumber, Integer status);

    Integer selectOrderStatusByUser(Integer userId, String orderNumber);
}
