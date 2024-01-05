package com.nft.trigger;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = TriggerApplicationTests.class)
class TriggerApplicationTests {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Test
    void contextLoads() {
        String queueName = "direct5.queue55";
        String message = "hello, spring amqp!";

        rabbitTemplate.convertAndSend(queueName, message);
    }

}
