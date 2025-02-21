package com.noodlestar.noodlestar.ReportSubdomain.PresentationLayer;


import com.noodlestar.noodlestar.ReportSubdomain.BusinessLayer.ReportService;
import com.noodlestar.noodlestar.ReportSubdomain.DataLayer.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Endpoint to generate a report for the most popular menu item.
     *
     * @return Mono<Report> containing the generated report.
     */
    @PostMapping("/generate")
    public Flux<ReportResponseModel> generateMostPopularMenuItemReport() {
        return reportService.generateMenuItemOrderFrequencyReport();
    }

    @PostMapping("/generate/financial")
    public Mono<ResponseEntity<ReportResponseModel>> generateFinancialReport() {
        return reportService.generateFinancialReport()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/generate/daily-orders")
    public Flux<ReportResponseModel> generateDailyOrderReport() {
        return reportService.generateDailyOrderReport();
    }


}
