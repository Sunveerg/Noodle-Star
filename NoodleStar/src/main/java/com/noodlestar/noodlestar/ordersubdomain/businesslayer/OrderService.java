package com.noodlestar.noodlestar.ordersubdomain.businesslayer;

import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderRequestModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderResponseModel;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface OrderService {

    Flux<OrderResponseModel> getAllOrders();

    Mono<OrderResponseModel> createOrder(Mono<OrderRequestModel> orderRequestDTO);

    Mono<OrderResponseModel> getOrderById(String orderId);

    Mono<Void> cancelOrder(String orderId);

    Flux<OrderResponseModel> getOrdersByCustomerId(String customerId);
    Mono<OrderResponseModel> updateOrderStatus(String orderId, String newStatus);
}

