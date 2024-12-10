package com.noodlestar.noodlestar.ordersubdomain.presentationlayer;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderDetailsRequestModel {
    private UUID menuId;
    private int quantity;
    private double price;
}
