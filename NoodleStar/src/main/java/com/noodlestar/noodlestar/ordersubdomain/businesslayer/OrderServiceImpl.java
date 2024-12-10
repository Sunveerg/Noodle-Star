package com.noodlestar.noodlestar.ordersubdomain.businesslayer;

import com.noodlestar.noodlestar.ordersubdomain.datalayer.Order;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderDetails;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderRepository;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderDetailsResponseModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderRequestModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderResponseModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Flux<OrderResponseModel> getAllOrders() {
        return orderRepository.findAll()
                .map(order -> {
                    List<OrderDetailsResponseModel> orderDetailsResponseModels = order.getOrderDetails().stream()
                            .map(orderDetails -> new OrderDetailsResponseModel(
                                    UUID.randomUUID(),
                                    orderDetails.getMenuId(),
                                    orderDetails.getQuantity(),
                                    orderDetails.getPrice()))
                            .collect(Collectors.toList());

                    return new OrderResponseModel(
                            order.getOrderId(),
                            order.getCustomerId(),
                            order.getStatus(),
                            order.getOrderDate(),
                            orderDetailsResponseModels,
                            order.getTotal()
                    );
                });
    }
    
    @Override
    public Mono<OrderResponseModel> createOrder(OrderRequestModel orderRequestModel) {
        List<OrderDetails> orderDetailsList = orderRequestModel.getOrderDetails().stream()
                .map(details -> new OrderDetails(
                        details.getMenuId(),
                        details.getQuantity(),
                        details.getPrice()))
                .collect(Collectors.toList());

        Order order = new Order(
                UUID.randomUUID(),
                orderRequestModel.getCustomerId(),
                "Pending",
                LocalDate.now(),
                orderDetailsList,
                0
        );

        return orderRepository.save(order)
                .map(savedOrder -> {
                    double total = savedOrder.getOrderDetails().stream()
                            .mapToDouble(orderDetails -> orderDetails.getPrice() * orderDetails.getQuantity())
                            .sum();
                    savedOrder.setTotal(total);

                    return new OrderResponseModel(
                            savedOrder.getOrderId(),
                            savedOrder.getCustomerId(),
                            savedOrder.getStatus(),
                            savedOrder.getOrderDate(),
                            savedOrder.getOrderDetails().stream()
                                    .map(details -> new OrderDetailsResponseModel(
                                            UUID.randomUUID(),
                                            details.getMenuId(),
                                            details.getQuantity(),
                                            details.getPrice()))
                                    .collect(Collectors.toList()),
                            total
                    );
                });
    }
}

