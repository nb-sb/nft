package com.nft.domain.common.mq;

/**
 * MQ操作接口
 */
public interface MqOperations {
    void SendCheckMessage(String orderId);
}
