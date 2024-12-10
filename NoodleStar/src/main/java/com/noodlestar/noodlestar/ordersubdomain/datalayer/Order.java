package com.noodlestar.noodlestar.ordersubdomain.datalayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Document(collection = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    private UUID orderId;
    private UUID customerId;
    private String status;
    private LocalDate orderDate;
    private List<OrderDetails> orderDetails;
    private double total;
}
