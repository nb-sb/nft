package com.nft.domain.order.respository;

import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.OrderInfoVo;
/**
* @author: 戏人看戏
* @Date: 2024/1/7 16:33
* @Description: 订单的存贮操作
*/
import java.util.Date;
import java.util.List;

public interface IOrderInfoRespository {

    //添加订单表
    boolean addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid, String OrderNo, Date time);

    OrderInfoVo selectOrderInfoByNumber(String orderNumber);

    //查询用户指定商品订单
    List<OrderInfoVo> selectOrderInfoByUser(Integer userId, Integer collectionId, Integer OrderStatus);


    //设置订单支付状态
    boolean setPayOrderStatus(String orderNumber, Integer status);

    boolean setOrderStatus(String orderNumber, Integer status);

    //查询订单状态
    Integer getOrderStatus(String orderNumber);
    //查询订单信息
    OrderInfoVo getOrder(String orderNumber);
    Integer selectOrderStatusByUser(Integer userId, String orderNumber);
}
