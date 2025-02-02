package com.noodlestar.noodlestar.ReportSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.ReportSubdomain.DataLayer.Report;
import com.noodlestar.noodlestar.ReportSubdomain.DataLayer.ReportRepository;
import com.noodlestar.noodlestar.ReportSubdomain.PresentationLayer.ReportResponseModel;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.Order;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderDetails;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceUnitTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReportRepository reportRepository;



    @Test
    public void whenGenerateMenuItemOrderFrequencyReport_thenReportsAreGenerated() {
        // Arrange
        List<OrderDetails> orderDetails1 = Arrays.asList(
                OrderDetails.builder().menuId("menuId1").quantity(3).price(10.0).build(),
                OrderDetails.builder().menuId("menuId2").quantity(5).price(15.0).build()
        );

        List<OrderDetails> orderDetails2 = Arrays.asList(
                OrderDetails.builder().menuId("menuId1").quantity(2).price(10.0).build(),
                OrderDetails.builder().menuId("menuId3").quantity(7).price(12.0).build()
        );

        Order order1 = Order.builder()
                .id(UUID.randomUUID().toString())
                .orderId(UUID.randomUUID().toString())
                .customerId("customer1")
                .status("COMPLETED")
                .orderDate(LocalDate.now())
                .orderDetails(orderDetails1)
                .total(95.0)
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID().toString())
                .orderId(UUID.randomUUID().toString())
                .customerId("customer2")
                .status("COMPLETED")
                .orderDate(LocalDate.now())
                .orderDetails(orderDetails2)
                .total(134.0)
                .build();

        // Mocking the repository interactions
        when(orderRepository.findAll()).thenReturn(Flux.just(order1, order2));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
            Report report = invocation.getArgument(0);
            return Mono.just(report); // Return the same report that was passed in
        });

        // Act
        Flux<ReportResponseModel> result = reportService.generateMenuItemOrderFrequencyReport();

        // Assert
        StepVerifier.create(result)
                .expectNextCount(3) // Expect exactly 3 reports
                .thenConsumeWhile(report -> {
                    // Validate each report without enforcing order
                    if (report.getMenuItemName().equals("menuId1")) {
                        assertThat(report.getReportType()).isEqualTo("Menu Item Frequency Report");
                        assertThat(report.getItemCount()).isEqualTo(5L);
                    } else if (report.getMenuItemName().equals("menuId2")) {
                        assertThat(report.getReportType()).isEqualTo("Menu Item Frequency Report");
                        assertThat(report.getItemCount()).isEqualTo(5L);
                    } else if (report.getMenuItemName().equals("menuId3")) {
                        assertThat(report.getReportType()).isEqualTo("Menu Item Frequency Report");
                        assertThat(report.getItemCount()).isEqualTo(7L);
                    } else {
                        fail("Unexpected menuItemName: " + report.getMenuItemName());
                    }
                    return true; // Continue consuming
                })
                .expectComplete() // Ensure the Flux completes
                .verify();

        verify(orderRepository, times(1)).findAll();
        verify(reportRepository, times(3)).save(any(Report.class)); // Verify save was called three times
    }

    @Test
    public void whenGenerateFinancialReport_thenTotalRevenueIsCalculated() {
        // Arrange
        Order order1 = Order.builder()
                .id(UUID.randomUUID().toString())
                .orderId(UUID.randomUUID().toString())
                .customerId("customer1")
                .status("COMPLETED")
                .orderDate(LocalDate.now())
                .total(95.0)
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID().toString())
                .orderId(UUID.randomUUID().toString())
                .customerId("customer2")
                .status("COMPLETED")
                .orderDate(LocalDate.now())
                .total(134.0)
                .build();

        Order order3 = Order.builder()
                .id(UUID.randomUUID().toString())
                .orderId(UUID.randomUUID().toString())
                .customerId("customer3")
                .status("COMPLETED")
                .orderDate(LocalDate.now())
                .total(71.5)
                .build();

        double expectedTotalRevenue = 95.0 + 134.0 + 71.5; // 300.5

        // Mocking repository calls
        when(orderRepository.findAll()).thenReturn(Flux.just(order1, order2, order3));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
            Report report = invocation.getArgument(0);
            return Mono.just(report); // Return the same report for verification
        });

        // Act
        Mono<ReportResponseModel> result = reportService.generateFinancialReport();

        // Assert
        StepVerifier.create(result)
                .assertNext(report -> {
                    assertThat(report.getReportType()).isEqualTo("Financial Report");
                    assertThat(report.getMenuItemName()).isEqualTo("Total Revenue");
                    assertThat(report.getItemCount()).isEqualTo((long) expectedTotalRevenue);
                })
                .expectComplete()
                .verify();

        verify(orderRepository, times(1)).findAll();
        verify(reportRepository, times(1)).save(any(Report.class)); // Should be called once for total revenue report
    }

}
