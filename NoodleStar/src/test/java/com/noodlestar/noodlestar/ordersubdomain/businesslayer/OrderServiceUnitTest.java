package com.noodlestar.noodlestar.ordersubdomain.businesslayer;

import static org.junit.jupiter.api.Assertions.*;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;

import com.noodlestar.noodlestar.ordersubdomain.datalayer.Order;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderDetails;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderRepository;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderDetailsRequestModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderRequestModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderResponseModel;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishDescriptionException;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishNameException;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishPriceException;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
class OrderServiceUnitTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenCreateOrder_thenReturnOrderResponseModel() {
        OrderDetailsRequestModel orderDetailsRequestModel = new OrderDetailsRequestModel();
        orderDetailsRequestModel.setMenuId("menu1");
        orderDetailsRequestModel.setQuantity(2);

        OrderRequestModel orderRequestModel = new OrderRequestModel();
        orderRequestModel.setCustomerId("customer1");
        orderRequestModel.setOrderDetails(Collections.singletonList(orderDetailsRequestModel));

        Order order = EntityDTOUtil.toOrderEntity(orderRequestModel);
        order.setOrderId("order1");
        order.setOrderDate(LocalDate.now());
        order.setTotal(25.98);

        Menu menu1 = Menu.builder()
                .id(UUID.randomUUID().toString())
                .menuId("menu1")
                .name("Spaghetti Carbonara")
                .description("Classic Italian pasta with egg, cheese, pancetta, and pepper.")
                .price(12.99)
                .category("Pasta")
                .itemImage("https://www.recipetineats.com/tachyon/2016/09/Wontons_4.jpg")
                .status(Status.AVAILABLE)
                .build();

        when(menuRepository.findMenuByMenuId("menu1")).thenReturn(Mono.just(menu1));
        when(orderRepository.insert(any(Order.class))).thenReturn(Mono.just(order));

        Mono<OrderResponseModel> result = orderService.createOrder(Mono.just(orderRequestModel));

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getOrderId().equals("order1") && response.getTotal() == 25.98)
                .verifyComplete();
    }

    @Test
    public void whenGetAllOrders_thenReturnOrders() {
        Order order1 = new Order();
        order1.setOrderId("order1");
        order1.setCustomerId("customer1");
        order1.setOrderDate(LocalDate.now());
        order1.setOrderDetails(Collections.singletonList(new OrderDetails("menu1", 2, 10.0)));
        order1.setTotal(20.0);

        Order order2 = new Order();
        order2.setOrderId("order2");
        order2.setCustomerId("customer2");
        order2.setOrderDate(LocalDate.now());
        order2.setOrderDetails(Collections.singletonList(new OrderDetails("menu2", 1, 15.0)));
        order2.setTotal(15.0);

        Order order3 = new Order();
        order3.setOrderId("order3");
        order3.setCustomerId("customer3");
        order3.setOrderDate(LocalDate.now());
        order3.setOrderDetails(Collections.singletonList(new OrderDetails("menu3", 3, 5.0)));
        order3.setTotal(15.0);

        when(orderRepository.findAll()).thenReturn(Flux.just(order1, order2, order3));

        Flux<OrderResponseModel> result = orderService.getAllOrders();

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getOrderId().equals("order1") && response.getTotal() == 20.0)
                .expectNextMatches(response -> response.getOrderId().equals("order2") && response.getTotal() == 15.0)
                .expectNextMatches(response -> response.getOrderId().equals("order3") && response.getTotal() == 15.0)
                .verifyComplete();
    }
}