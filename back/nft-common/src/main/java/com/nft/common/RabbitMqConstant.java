package com.nft.common;

public interface RabbitMqConstant {
    //延时消息交换机
    String DELAY_DERECT = "delay.derect";
    //延时消息队列
    String DELAY_QUEUE = "delay.queue";
    //延时消息key
    String ORDER_CHECK_STATUS = "orderCheckStatus";
    //延迟时间
    Integer MINUTE_30 = 60 * 30 * 1000;
    Integer MINUTE_60 = 60 * 60 * 1000;
    Integer MINUTE_1 = 60 * 1 * 1000;
    Integer SECOND_1 = 1 * 1000;

}
