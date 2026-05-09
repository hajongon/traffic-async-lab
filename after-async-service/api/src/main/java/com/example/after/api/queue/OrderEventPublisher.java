package com.example.after.api.queue;

import com.example.after.api.order.OrderEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public OrderEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.queue.exchange}") String exchange,
            @Value("${app.queue.routing-key}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publish(OrderEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
