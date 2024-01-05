package com.nft.common.Rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmq.listener.simple",name = "retry.enabled",havingValue = "true")
public class ErrorConfiguration {
//    这里是错误处理的交换机发送的类
    /*
    * 当出现错误时,进行重试发送,若重试发送还是不成功将会记录到这个交换机中
    * */
    public static final String RETRY_FAILURE_KEY = "error";
    public static final String RETRY_EXCHANGE = "error.direct";
    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange(RETRY_EXCHANGE);
    }
    @Bean
    public Queue errorQueue() {
        return new Queue("error.queue");
    }
    @Bean
    public Binding ErrorBinding() {
        return BindingBuilder.bind(errorQueue()).to(errorExchange()).with(RETRY_FAILURE_KEY);
    }
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate,RETRY_EXCHANGE,RETRY_FAILURE_KEY);
    }
}
