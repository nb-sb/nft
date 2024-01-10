package com.nft.domain.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Result;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.user.model.vo.UserVo;

public interface INftOrderService {

    //购买藏品
    Result addConllectionOrder(UserVo fromUser, Integer ConllectionID);

    //支付订单
    Result payOrder(UserVo fromUser,String OrderNumber,Integer paytype);

    //修改商品信息 -1.可以在用户购买商品的时候用于更新剩余信息等 2.管理员更新藏品信息的时候用
    Result updataConllectionInfo(UpdataCollectionReq updataCollectionReq);

    //管理员 查询所有订单
    Result selectAllOrder(Page page);

    //查询自己的订单
    Result getOrder(Integer userId);
    //查询用户给指定订单信息
    Result getOrder(Integer userId,String orderId);
    //查询自己待支付/已支付等状态订单
    Result getOrderByStatus(Integer userId, Integer payOrderStatus);
}
