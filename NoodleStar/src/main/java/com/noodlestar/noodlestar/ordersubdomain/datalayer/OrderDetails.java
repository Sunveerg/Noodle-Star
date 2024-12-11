package com.noodlestar.noodlestar.ordersubdomain.datalayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetails {
    private String orderId;
    private String menuId;
    private int quantity;
    private double price;
}
