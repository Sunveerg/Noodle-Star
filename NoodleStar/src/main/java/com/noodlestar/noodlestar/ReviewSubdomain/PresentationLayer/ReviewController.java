package com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer.ReviewService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/review")
@Validated
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping()
    public Flux<ReviewResponseModel> getAllReviews() {
        return reviewService.getAllReview();
    }
}
