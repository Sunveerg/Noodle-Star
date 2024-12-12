package com.noodlestar.noodlestar.ordersubdomain.presentationlayer;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderResponseModel {
    private String orderId;
    private String customerId;
    private String status;
    private LocalDate orderDate;
    private List<OrderDetailsResponseModel> orderDetails;
    private double total;
}
