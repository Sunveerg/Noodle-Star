package com.noodlestar.noodlestar.ordersubdomain.presentationlayer;

import com.noodlestar.noodlestar.ordersubdomain.businesslayer.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<OrderResponseModel> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Mono<OrderResponseModel> createOrder(@RequestBody OrderRequestModel orderRequestDTO) {
        return orderService.createOrder(orderRequestDTO);
    }
}
