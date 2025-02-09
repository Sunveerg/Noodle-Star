package com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.Review;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewRequestModel;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewResponseModel;
import com.noodlestar.noodlestar.ordersubdomain.presentationlayer.OrderResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {
    Flux<ReviewResponseModel> getAllReview();

    Mono<ReviewResponseModel> addReview(Mono<ReviewRequestModel> reviewRequestModel);

    Flux<ReviewResponseModel> getReviewsByUserId(String userId);
    Mono<Void> deleteReview(String reviewId);

    Mono<ReviewResponseModel> updateReview(Mono<ReviewRequestModel> reviewRequestModel, String reviewId);

    Mono<ReviewResponseModel> getReviewById(String reviewId);
}
