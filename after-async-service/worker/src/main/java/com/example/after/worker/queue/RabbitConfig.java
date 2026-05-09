package com.example.after.worker.queue;

import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    DirectExchange ordersExchange(@Value("${app.queue.exchange}") String exchange) {
        return new DirectExchange(exchange);
    }

    @Bean
    DirectExchange deadLetterExchange(@Value("${app.queue.dead-letter-exchange}") String exchange) {
        return new DirectExchange(exchange);
    }

    @Bean
    Queue ordersQueue(
            @Value("${app.queue.queue}") String queue,
            @Value("${app.queue.dead-letter-exchange}") String deadLetterExchange,
            @Value("${app.queue.dead-letter-routing-key}") String deadLetterRoutingKey
    ) {
        return new Queue(queue, true, false, false, Map.of(
                "x-dead-letter-exchange", deadLetterExchange,
                "x-dead-letter-routing-key", deadLetterRoutingKey
        ));
    }

    @Bean
    Queue deadLetterQueue(@Value("${app.queue.dead-letter-queue}") String queue) {
        return new Queue(queue, true);
    }

    @Bean
    Binding ordersBinding(Queue ordersQueue, DirectExchange ordersExchange, @Value("${app.queue.routing-key}") String routingKey) {
        return BindingBuilder.bind(ordersQueue).to(ordersExchange).with(routingKey);
    }

    @Bean
    Binding deadLetterBinding(
            Queue deadLetterQueue,
            DirectExchange deadLetterExchange,
            @Value("${app.queue.dead-letter-routing-key}") String routingKey
    ) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(routingKey);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
