package com.nft.app.mq.producer;

import cn.hutool.json.JSONUtil;
import com.nft.common.Rabbitmq.RabbitMqConstant;
import com.nft.domain.order.model.req.AddOrderMqMessage;
import com.nft.domain.support.mq.MqOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderPublisher  {
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
                    message.getMessageProperties().setDelay(RabbitMqConstant.MINUTE_30);
                    return message;
                });
        log.info("消息发送成功 orderId: "+orderId);
    }

    public void SendAddEsMessage(String jsonStr) {
        rabbitTemplate.convertAndSend(RabbitMqConstant.ADDES_DERECT, RabbitMqConstant.ADDES_KEY, jsonStr);
        log.info("消息发送成功 addes jsonStr: "+jsonStr);
    }

    public void SendAddOrderMessage(AddOrderMqMessage addOrderMqMessage) {
        String jsonStr = JSONUtil.toJsonStr(addOrderMqMessage);
        rabbitTemplate.convertAndSend(RabbitMqConstant.ADD_ORDER_DERECT, RabbitMqConstant.ADD_ORDER_KEY, jsonStr);
        log.info("消息发送成功 SendAddOrderMessage: "+jsonStr);
    }
}
