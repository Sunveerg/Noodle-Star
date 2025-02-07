package com.noodlestar.noodlestar.ReviewSubdomain.DataLayer;

import com.noodlestar.noodlestar.ordersubdomain.datalayer.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepo extends ReactiveMongoRepository<Review, String> {

    Mono<Review> findReviewByReviewId(String reviewId);

    Mono<Review> findReviewByIdAndReviewerName(String reviewId, String reviewerName);

    Flux<Review> findAllByUserId(String userId);
}
