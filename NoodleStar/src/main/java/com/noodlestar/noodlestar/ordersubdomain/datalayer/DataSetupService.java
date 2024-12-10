package com.noodlestar.noodlestar.ordersubdomain.datalayer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataSetupService implements CommandLineRunner {

    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        setupOrders();
    }

    private void setupOrders() {
        Order order1 = buildOrder(UUID.randomUUID(), UUID.fromString("11111111-1111-1111-1111-111111111111"), "Pending", LocalDate.now(),
                buildOrderDetails(UUID.fromString("menuId1"), 2, 11.99), buildOrderDetails(UUID.fromString("menuId2"), 1, 5.99));
        Order order2 = buildOrder(UUID.randomUUID(), UUID.fromString("22222222-2222-2222-2222-222222222222"), "Completed", LocalDate.now().minusDays(1),
                buildOrderDetails(UUID.fromString("menuId3"), 1, 15.99), buildOrderDetails(UUID.fromString("menuId4"), 3, 15.99));
        Order order3 = buildOrder(UUID.randomUUID(), UUID.fromString("33333333-3333-3333-3333-333333333333"), "Pending", LocalDate.now().minusDays(2),
                buildOrderDetails(UUID.fromString("menuId5"), 1, 12.99), buildOrderDetails(UUID.fromString("menuId6"), 4, 4.99));
        Order order4 = buildOrder(UUID.randomUUID(), UUID.fromString("44444444-4444-4444-4444-444444444444"), "Canceled", LocalDate.now().minusDays(3),
                buildOrderDetails(UUID.fromString("menuId7"), 2, 18.99), buildOrderDetails(UUID.fromString("menuId8"), 1, 13.49));
        Order order5 = buildOrder(UUID.randomUUID(), UUID.fromString("55555555-5555-5555-5555-555555555555"), "Pending", LocalDate.now(),
                buildOrderDetails(UUID.fromString("menuId9"), 5, 7.99), buildOrderDetails(UUID.fromString("menuId10"), 3, 14.49));

        Mono.when(
                orderRepository.insert(order1),
                orderRepository.insert(order2),
                orderRepository.insert(order3),
                orderRepository.insert(order4),
                orderRepository.insert(order5)
        ).subscribe();
    }

    private Order buildOrder(UUID orderId, UUID customerId, String status, LocalDate orderDate, OrderDetails... orderDetails) {
        double total = 0;
        for (OrderDetails details : orderDetails) {
            total += details.getQuantity() * details.getPrice();
        }

        return Order.builder()
                .orderId(orderId)
                .customerId(customerId)
                .status(status)
                .orderDate(orderDate)
                .orderDetails(List.of(orderDetails))
                .total(total)
                .build();
    }

    private OrderDetails buildOrderDetails(UUID menuId, int quantity, double price) {
        return OrderDetails.builder()
                .menuId(menuId)
                .quantity(quantity)
                .price(price)
                .build();
    }
}
