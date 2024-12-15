package com.noodlestar.noodlestar.ordersubdomain.presentationlayer;

import com.noodlestar.noodlestar.ordersubdomain.businesslayer.OrderService;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderDetails;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;

class OrderControllerUnitTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        webTestClient = WebTestClient.bindToController(orderController).build(); // Bind controller manually
    }

    @Test
    void getAllOrders() {
        OrderResponseModel order1 = new OrderResponseModel();
        order1.setOrderId("order1");
        order1.setCustomerId("customer1");
        order1.setOrderDate(LocalDate.now());
        order1.setOrderDetails(Collections.singletonList(new OrderDetailsResponseModel("menu1", 2)));
        order1.setTotal(20.0);

        OrderResponseModel order2 = new OrderResponseModel();
        order2.setOrderId("order2");
        order2.setCustomerId("customer2");
        order2.setOrderDate(LocalDate.now());
        order2.setOrderDetails(Collections.singletonList(new OrderDetailsResponseModel("menu2", 1)));
        order2.setTotal(15.0);

        OrderResponseModel order3 = new OrderResponseModel();
        order3.setOrderId("order3");
        order3.setCustomerId("customer3");
        order3.setOrderDate(LocalDate.now());
        order3.setOrderDetails(Collections.singletonList(new OrderDetailsResponseModel("menu3", 3)));
        order3.setTotal(15.0);

        when(orderService.getAllOrders()).thenReturn(Flux.just(order1, order2, order3));

        webTestClient.get()
                .uri("/api/v1/orders")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderResponseModel.class)
                .hasSize(3)
                .contains(order1, order2, order3);

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getOrderById() {
        OrderResponseModel order = new OrderResponseModel();
        order.setOrderId("order1");
        order.setCustomerId("customer1");
        order.setOrderDate(LocalDate.now());
        order.setOrderDetails(Collections.singletonList(new OrderDetailsResponseModel("menu1", 2)));
        order.setTotal(20.0);

        when(orderService.getOrderById("order1")).thenReturn(Mono.just(order));

        webTestClient.get()
                .uri("/api/v1/orders/order1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderResponseModel.class)
                .isEqualTo(order);

        verify(orderService, times(1)).getOrderById("order1");
    }

    @Test
    void createOrder() {
        OrderRequestModel orderRequest = new OrderRequestModel();
        orderRequest.setCustomerId("customer1");
        orderRequest.setOrderDetails(Collections.singletonList(new OrderDetailsRequestModel("menu1", 2)));

        OrderResponseModel orderResponse = new OrderResponseModel();
        orderResponse.setOrderId("order1");
        orderResponse.setCustomerId("customer1");
        orderResponse.setOrderDate(LocalDate.now());
        orderResponse.setOrderDetails(Collections.singletonList(new OrderDetailsResponseModel("menu1", 2)));
        orderResponse.setTotal(20.0);

        when(orderService.createOrder(any(Mono.class))).thenReturn(Mono.just(orderResponse));

        webTestClient.post()
                .uri("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(orderRequest), OrderRequestModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderResponseModel.class)
                .isEqualTo(orderResponse);

        verify(orderService, times(1)).createOrder(any(Mono.class));
    }

    @Test
    void cancelOrder() {
        String orderId = "order1";

        when(orderService.cancelOrder(orderId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/orders/{orderId}", orderId)
                .exchange()
                .expectStatus().isOk();

        verify(orderService, times(1)).cancelOrder(orderId);
    }


}