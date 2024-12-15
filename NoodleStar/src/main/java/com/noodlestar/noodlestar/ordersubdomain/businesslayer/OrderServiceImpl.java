package com.noodlestar.noodlestar.ordersubdomain.businesslayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.Order;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderRepository;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderRequestModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderResponseModel;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.MenuItemDoesNotExistException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    public OrderServiceImpl(OrderRepository orderRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public Flux<OrderResponseModel> getAllOrders() {
        return orderRepository.findAll()
                .map(EntityDTOUtil::toOrderResponseModel);
    }

    @Override
    public Mono<OrderResponseModel> getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(EntityDTOUtil::toOrderResponseModel);
    }

    @Override
    public Mono<OrderResponseModel> createOrder(Mono<OrderRequestModel> orderRequestModel) {
        return orderRequestModel
                .flatMap(request -> {
                    Order order = EntityDTOUtil.toOrderEntity(request);
                    order.setOrderId(EntityDTOUtil.generateOrderIdString());
                    order.setOrderDate(LocalDate.now());
                    order.setStatus(request.getStatus() != null ? request.getStatus() : "Pending");

                    return validateMenuItems(request)
                            .then(calculateTotal(request))
                            .flatMap(total -> {
                                order.setTotal(total);
                                return orderRepository.insert(order)
                                        .map(EntityDTOUtil::toOrderResponseModel);
                            });
                });
    }

    private Mono<Void> validateMenuItems(OrderRequestModel request) {
        return Flux.fromIterable(request.getOrderDetails())
                .flatMap(details -> menuRepository.findMenuByMenuId(details.getMenuId())
                        .switchIfEmpty(Mono.error(new MenuItemDoesNotExistException("Menu item with ID " + details.getMenuId() + " does not exist"))))
                .then();
    }

    private Mono<Double> calculateTotal(OrderRequestModel request) {
        Flux<Mono<Double>> priceMonoFlux = Flux.fromIterable(request.getOrderDetails())
                .map(details -> menuRepository.findMenuByMenuId(details.getMenuId())
                        .map(menuItem -> menuItem.getPrice() * details.getQuantity()));

        return Flux.merge(priceMonoFlux)
                .reduce(0.0, Double::sum);
    }
}