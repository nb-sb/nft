package com.nft.domain.order.respository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.vo.OrderInfoVo;
import com.nft.domain.order.model.vo.UserOrderSimpleVo;

import java.util.List;

public interface INftOrderRespository {

    //查询所有订单
    List<OrderInfoVo> selectAllOrder(Page page);

    List<UserOrderSimpleVo> getOrder(Integer userId);

    List<OrderInfoVo> getOrder(Integer userId,String orderId);
    //查询用户拥有的商品订单列表





}
