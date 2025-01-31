package com.noodlestar.noodlestar.ReportSubdomain.DataLayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReportRepository extends ReactiveMongoRepository<Report, String> {

    Mono<Report> findByReportId(String reportId);
}
