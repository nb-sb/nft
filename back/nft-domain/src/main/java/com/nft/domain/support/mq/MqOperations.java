package com.nft.domain.support.mq;

import com.nft.domain.order.model.req.AddOrderMqMessage;

/**
 * MQ操作接口
 */
public interface MqOperations {
    void SendCheckMessage(String orderId);

    void SendAddEsMessage(String jsonStr);
    //发送添加订单信息消息
    void SendAddOrderMessage(AddOrderMqMessage addOrderMqMessage);
}
