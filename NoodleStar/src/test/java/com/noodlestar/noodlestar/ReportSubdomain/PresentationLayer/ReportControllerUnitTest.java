package com.noodlestar.noodlestar.ReportSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.ReportSubdomain.BusinessLayer.ReportService;
import com.noodlestar.noodlestar.ReportSubdomain.PresentationLayer.ReportResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class ReportControllerUnitTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        webTestClient = WebTestClient.bindToController(reportController).build(); // Bind controller manually
    }

    @Test
    void generateMostPopularMenuItemReport() {
        // Arrange
        LocalDateTime fixedDate = LocalDateTime.of(2024, 12, 10, 18, 38, 9, 0);

        // Create mock report data
        ReportResponseModel report1 = ReportResponseModel.builder()
                .reportId("REPORT-1")
                .reportType("Menu Item Frequency Report")
                .menuItemName("menuId1")
                .itemCount(5L)
                .generatedAt(fixedDate)
                .build();

        ReportResponseModel report2 = ReportResponseModel.builder()
                .reportId("REPORT-2")
                .reportType("Menu Item Frequency Report")
                .menuItemName("menuId2")
                .itemCount(3L)
                .generatedAt(fixedDate)
                .build();

        // Mock the service to return the mock data
        when(reportService.generateMenuItemOrderFrequencyReport()).thenReturn(Flux.just(report1, report2));

        // Act & Assert
        webTestClient.post()
                .uri("/api/reports/generate")  // URI to the controller endpoint
                .accept(MediaType.APPLICATION_JSON)  // Accept JSON response
                .exchange()
                .expectStatus().isOk()  // Assert that the status is OK
                .expectBodyList(ReportResponseModel.class)  // Assert that the response body is a list of ReportResponseModel
                .hasSize(2)  // Assert that the list has exactly 2 items
                .contains(report1, report2);  // Assert that the list contains the expected reports

        // Verify that the reportService method was called once
        verify(reportService, times(1)).generateMenuItemOrderFrequencyReport();
    }

    @Test
    void generateFinancialReport() {
        // Arrange
        LocalDateTime fixedDate = LocalDateTime.of(2024, 12, 10, 18, 38, 9, 0);

        // Create mock report data for the financial report
        ReportResponseModel financialReport = ReportResponseModel.builder()
                .reportId("FIN-REPORT-1")
                .reportType("Financial Report")
                .menuItemName("Total Revenue")
                .itemCount(5000L) // Example total revenue
                .generatedAt(fixedDate)
                .build();

        // Mock the service to return the mock financial report data
        when(reportService.generateFinancialReport()).thenReturn(Mono.just(financialReport));

        // Act & Assert
        webTestClient.post()
                .uri("/api/reports/generate/financial")  // URI to the financial report endpoint
                .accept(MediaType.APPLICATION_JSON)  // Accept JSON response
                .exchange()
                .expectStatus().isOk()  // Assert that the status is OK
                .expectBody(ReportResponseModel.class)  // Assert that the response body is a single ReportResponseModel
                .isEqualTo(financialReport);  // Assert that the response is the same as the expected financial report

        // Verify that the reportService method was called once
        verify(reportService, times(1)).generateFinancialReport();
    }
}