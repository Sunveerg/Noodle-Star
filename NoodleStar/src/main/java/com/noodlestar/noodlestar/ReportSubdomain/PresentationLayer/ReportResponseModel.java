package com.noodlestar.noodlestar.ReportSubdomain.PresentationLayer;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponseModel {
     String reportId;

     String reportType; // e.g., "Most Popular Menu Item"

     String menuItemName; // The most popular menu item

     Long itemCount; // The number of times this item was ordered

     LocalDateTime generatedAt;
}
