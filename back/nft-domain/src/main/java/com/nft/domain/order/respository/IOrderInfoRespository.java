package com.nft.domain.order.respository;

import com.nft.domain.nft.model.vo.OrderInfoVo;
import com.nft.domain.order.model.entity.OrderEntity;
/**
* @author: 戏人看戏
* @Date: 2024/1/7 16:33
* @Description: 订单的存贮操作
*/
import java.util.List;

public interface IOrderInfoRespository {

    //添加订单表
    boolean creat(OrderEntity orderEntity);

    OrderEntity selectOrderInfoByNumber(String orderNumber);

    //查询用户指定商品订单
    List<OrderInfoVo> selectOrderInfoByUser(Integer userId, Integer collectionId, Integer OrderStatus);


    //设置订单支付状态
    boolean save(OrderEntity orderEntity);

    //查询订单状态
    Integer getOrderStatus(String orderNumber);

    //查询订单信息
    Integer selectOrderStatusByUser(Integer userId, String orderNumber);
}
