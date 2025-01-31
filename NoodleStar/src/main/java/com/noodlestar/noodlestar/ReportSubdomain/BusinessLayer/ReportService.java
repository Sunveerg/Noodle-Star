package com.noodlestar.noodlestar.ReportSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.ReportSubdomain.PresentationLayer.ReportResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportService {
    Flux<ReportResponseModel> generateMenuItemOrderFrequencyReport();
    Mono<ReportResponseModel> generateFinancialReport();

}
