package com.nft.infrastructure.event.mq.producer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * 设置延迟发送的属性
 */
@RequiredArgsConstructor
public class DelayMessageProcessor implements MessagePostProcessor {

    private final int delay;
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setDelay(delay);
        return message;
    }
}
