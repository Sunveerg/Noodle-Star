package com.noodlestar.noodlestar.ReportSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.ReportSubdomain.DataLayer.Report;
import com.noodlestar.noodlestar.ReportSubdomain.DataLayer.ReportRepository;
import com.noodlestar.noodlestar.ReportSubdomain.PresentationLayer.ReportResponseModel;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {


    private final OrderRepository orderRepository;
    private final ReportRepository reportRepository;

    public ReportServiceImpl(OrderRepository orderRepository, ReportRepository reportRepository) {
        this.orderRepository = orderRepository;
        this.reportRepository = reportRepository;
    }


    @Override
    public Flux<ReportResponseModel> generateMenuItemOrderFrequencyReport() {
        return orderRepository.findAll() // Fetch all orders
                .flatMap(order -> Flux.fromIterable(order.getOrderDetails())) // Flatten order details
                .groupBy(orderDetails -> orderDetails.getMenuId()) // Group by menuId
                .flatMap(groupedFlux -> groupedFlux
                        .reduce(0L, (total, details) -> total + details.getQuantity())
                        .map(total -> new ItemCount(groupedFlux.key(), total))) // Calculate total per menuId
                .sort(Comparator.comparing(ItemCount::getCount).reversed()) // Sort by count descending
                .flatMap(itemCount -> {
                    Report report = Report.builder()
                            .reportId("REPORT-" + System.currentTimeMillis())
                            .reportType("Menu Item Frequency Report")
                            .menuItemName(itemCount.getMenuId()) // Use menuId as placeholder for name
                            .itemCount(itemCount.getCount())
                            .generatedAt(LocalDateTime.now())
                            .build();

                    return reportRepository.save(report) // Save each item as an individual report entry
                            .map(savedReport -> new ReportResponseModel(
                                    savedReport.getReportId(),
                                    savedReport.getReportType(),
                                    savedReport.getMenuItemName(),
                                    savedReport.getItemCount(),
                                    savedReport.getGeneratedAt()
                            ));
                });
    }


    private static class ItemCount {
        private final String menuId;
        private final Long count;

        public ItemCount(String menuId, Long count) {
            this.menuId = menuId;
            this.count = count;
        }

        public String getMenuId() {
            return menuId;
        }

        public Long getCount() {
            return count;
        }
    }
}
