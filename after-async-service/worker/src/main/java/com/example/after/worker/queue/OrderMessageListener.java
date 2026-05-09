package com.example.after.worker.queue;

import com.example.after.worker.order.OrderEvent;
import com.example.after.worker.order.OrderProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageListener {
    private final OrderProcessor orderProcessor;

    public OrderMessageListener(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @RabbitListener(queues = "${app.queue.queue}")
    public void handle(OrderEvent event) {
        orderProcessor.process(event);
    }
}
