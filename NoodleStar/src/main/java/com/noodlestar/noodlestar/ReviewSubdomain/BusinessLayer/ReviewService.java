package com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.Review;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {
    Flux<ReviewResponseModel> getAllReview();
}
