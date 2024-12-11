package com.noodlestar.noodlestar.ordersubdomain.presentationlayer;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderDetailsResponseModel {
    private String menuId;
    private int quantity;
}
