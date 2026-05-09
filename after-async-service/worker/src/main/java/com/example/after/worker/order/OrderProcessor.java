package com.example.after.worker.order;

import com.example.after.worker.notification.NotificationService;
import com.example.after.worker.payment.PaymentService;
import com.example.after.worker.stock.StockCompensationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessor {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    private final StockCompensationService stockCompensationService;

    public OrderProcessor(
            OrderRepository orderRepository,
            PaymentService paymentService,
            NotificationService notificationService,
            StockCompensationService stockCompensationService
    ) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
        this.stockCompensationService = stockCompensationService;
    }

    @Transactional
    public void process(OrderEvent event) {
        Order order = orderRepository.findById(event.orderId()).orElseThrow();
        order.processing();
        try {
            paymentService.pay(order.getId());
            notificationService.send(order.getId());
            order.complete();
        } catch (RuntimeException e) {
            order.fail(e.getMessage());
            stockCompensationService.compensate(event.productId());
            throw e;
        }
    }
}
