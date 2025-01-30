package com.noodlestar.noodlestar.ReportSubdomain.DataLayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {
    @Id
    private String id;
    private String reportId;

    private String reportType; // e.g., "Most Popular Menu Item"

    private String menuItemName; // The most popular menu item

    private Long itemCount; // The number of times this item was ordered

    private LocalDateTime generatedAt;
}
