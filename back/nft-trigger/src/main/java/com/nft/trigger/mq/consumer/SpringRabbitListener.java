package com.nft.trigger.mq.consumer;

import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.RabbitMqConstant;
import com.nft.domain.nft.repository.IOrderInfoRespository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 监听队列
 */
@Component
@Log4j2
@AllArgsConstructor
public class SpringRabbitListener {
    private final IOrderInfoRespository iOrderInfoRespository;
    /**
     * @Des 订单状态检查
     * @Date 2024/1/5 15:43
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMqConstant.DELAY_QUEUE, declare = "true"),
            exchange = @Exchange(name = RabbitMqConstant.DELAY_DERECT, delayed = "true"),
            key = RabbitMqConstant.ORDER_CHECK_STATUS ))
    public void ORDER_CHECK_STATUS(String orderId) {
        log.info("消息接受成功 orderId : "+orderId);
        //检查订单状态 如果未支付则取消，其他状态如取消订单等状态则不修改
        Integer orderStatus = iOrderInfoRespository.getOrderStatus(orderId);
        if (!Constants.payOrderStatus.NO_PAY.equals(orderStatus)) {
            //订单状态只能是未支付的，但是此时订单状态未：已支付/已取消
            log.info("订单状态无需修改 orderId : "+orderId +" status ： " + orderStatus);
            return;
        }
        //修改订单状态为已取消
        boolean b = iOrderInfoRespository.setOrderStatus(orderId, Constants.payOrderStatus.CANCEL);
        if (!b) {
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "修改订单状态为已取消 失败");
        }
    }

    //使用注解获取队列没有则会创建
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct5.queue55", declare = "true"),
            exchange = @Exchange(name = "nbsb.direct5", type = ExchangeTypes.DIRECT)
    ))
    public void test2(String msg) {
        System.out.println(msg);
    }

}
