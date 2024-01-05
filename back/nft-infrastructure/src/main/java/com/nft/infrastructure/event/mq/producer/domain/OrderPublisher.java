package com.nft.infrastructure.event.mq.producer.domain;

import com.nft.common.RabbitMqConstant;
import com.nft.domain.common.mq.MqOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderPublisher implements MqOperations {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @Des 发送检查消息订单抢购完成后进行发送的消息，用于30分钟后进行检查订单
     * @Date 2024/1/5 15:38
     * @Param orderId 订单id
     */
    public void SendCheckMessage(String orderId) {
        rabbitTemplate.convertAndSend(RabbitMqConstant.DELAY_DERECT, RabbitMqConstant.ORDER_CHECK_STATUS,
                orderId, message -> {
                    message.getMessageProperties().setDelay(RabbitMqConstant.MINUTE_1);
                    return message;
                });
        log.info("消息发送成功 orderId: "+orderId);
    }
}
