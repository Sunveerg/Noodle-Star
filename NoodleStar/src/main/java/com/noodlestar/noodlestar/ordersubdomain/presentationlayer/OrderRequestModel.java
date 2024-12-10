package com.noodlestar.noodlestar.ordersubdomain.presentationlayer;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderRequestModel {
    private UUID customerId;
    private List<OrderDetailsRequestModel> orderDetails;
}
