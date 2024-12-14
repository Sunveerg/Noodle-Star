package com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


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

    @PostMapping("")
    public Mono<ResponseEntity<ReviewResponseModel>> addDish(@RequestBody Mono<ReviewRequestModel> reviewRequestModel) {
        return reviewService.addReview(reviewRequestModel)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.just(
                ResponseEntity.badRequest().body(new ReviewResponseModel("Error", 0, "", e.getMessage(), null)))
        );
    }
}
