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
public class OrderRequestModel {
    private String customerId;
    private String status;
    private LocalDate orderDate;
    private List<OrderDetailsRequestModel> orderDetails;
    private double total;
}
