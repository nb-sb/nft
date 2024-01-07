package com.nft.domain.support.mq;

/**
 * MQ操作接口
 */
public interface MqOperations {
    void SendCheckMessage(String orderId);
}
