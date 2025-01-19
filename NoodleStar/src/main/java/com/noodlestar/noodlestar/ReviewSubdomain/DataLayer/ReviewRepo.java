package com.noodlestar.noodlestar.ReviewSubdomain.DataLayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepo extends ReactiveMongoRepository<Review, String> {

    Mono<Review> findReviewById(String reviewId);

    Mono<Review> findReviewByIdAndReviewerName(String reviewId, String reviewerName);
}
