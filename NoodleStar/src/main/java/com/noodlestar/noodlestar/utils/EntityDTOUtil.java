package com.noodlestar.noodlestar.utils;


import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.Review;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewRequestModel;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewResponseModel;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.Order;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderDetails;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderDetailsRequestModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderDetailsResponseModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderRequestModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EntityDTOUtil {


    public static ReviewResponseModel toReviewResponseDTO(Review review) {
        ReviewResponseModel reviewResponseDTO  = new ReviewResponseModel ();
        BeanUtils.copyProperties(review, reviewResponseDTO);
        return reviewResponseDTO;
    }

    public static Review toReviewEntity(ReviewRequestModel reviewRequestModel){
        return Review.builder()
                .reviewId(generateReviewIdString())
                .rating(reviewRequestModel.getRating())
                .reviewerName(reviewRequestModel.getReviewerName())
                .review(reviewRequestModel.getReview())
                .dateSubmitted(reviewRequestModel.getDateSubmitted())
                .build();
    }


    //    public static MenuResponseModel toMenuResponseDTO(Menu menu) {
//        MenuResponseModel menuResponseModel  = new MenuResponseModel ();
//        BeanUtils.copyProperties(menu, menuResponseModel);
//        return menuResponseModel;
//    }
//
//    public static Menu toMenuEntity(MenuRequestModel menuRequestModel){
//        return Menu.builder()
//                .menuId(generateMenuIdString())
//                .name(menuRequestModel.getName())
//                .description(menuRequestModel.getDescription())
//                .price(menuRequestModel.getPrice())
//                .category(menuRequestModel.getCategory())
//                .itemImage(menuRequestModel.getItemImage())
//                .status(menuRequestModel.getStatus())
//                .build();
//    }

    public static Menu toMenuEntity(MenuRequestModel menuRequest) {
        return Menu.builder()
                .name(menuRequest.getName())
                .price(menuRequest.getPrice())
                .description(menuRequest.getDescription())
                .category(menuRequest.getCategory())
                .itemImage(menuRequest.getItemImage())
                .status(menuRequest.getStatus())
                .build();
    }

    public static MenuResponseModel toMenuResponseDTO(Menu menu) {
        return MenuResponseModel.builder()
                .menuId(menu.getMenuId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .category(menu.getCategory())
                .itemImage(menu.getItemImage())
                .status(menu.getStatus())
                .build();
    }

    public static MenuResponseModel menuEntityToResponseDTO(Menu menu) {
        return MenuResponseModel.builder()
                .menuId(menu.getMenuId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .category(menu.getCategory())
                .itemImage(menu.getItemImage())
                .status(menu.getStatus())
                .build();
    }

    // Convert MenuRequestDTO to Menu entity
    public static Menu menuRequestDtoToEntity(MenuRequestModel dto) {
        return Menu.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .itemImage(dto.getItemImage())
                .status(dto.getStatus())
                .build();
    }

    public static String generateMenuIdString() {
        return UUID.randomUUID().toString();
    }


    public static String generateReviewIdString() {
        return UUID.randomUUID().toString();
    }

    public static Order toOrderEntity(OrderRequestModel orderRequest) {
        return Order.builder()
                .orderId(generateOrderIdString())
                .customerId(orderRequest.getCustomerId())
                .status(orderRequest.getStatus())
                .orderDate(orderRequest.getOrderDate())
                .orderDetails(toOrderDetailsList(orderRequest.getOrderDetails()))
                .total(orderRequest.getTotal())
                .build();
    }


    public static OrderResponseModel toOrderResponseModel(Order order) {
        return OrderResponseModel.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .orderDetails(toOrderDetailsResponseList(order.getOrderDetails()))
                .total(order.getTotal())
                .build();
    }
    private static List<OrderDetails> toOrderDetailsList(List<OrderDetailsRequestModel> orderDetailsRequestModels) {
        return orderDetailsRequestModels.stream()
                .map(details -> OrderDetails.builder()
                        .menuId(details.getMenuId())
                        .quantity(details.getQuantity())
                        .build())
                .toList();
    }

    private static List<OrderDetailsResponseModel> toOrderDetailsResponseList(List<OrderDetails> orderDetails) {
        return orderDetails.stream()
                .map(details -> OrderDetailsResponseModel.builder()
                        .menuId(details.getMenuId())
                        .quantity(details.getQuantity())
                        .build())
                .toList();
    }

    public static String generateOrderIdString() {
        return UUID.randomUUID().toString();
    }
}
