package com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer.ReviewService;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
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
    public Mono<ResponseEntity<ReviewResponseModel>> addReview(@RequestBody Mono<ReviewRequestModel> reviewRequestModel) {
        return reviewService.addReview(reviewRequestModel)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/{userId}")
    public Flux<ReviewResponseModel> getReviewByUserId(@PathVariable String userId) {
        return reviewService.getReviewsByUserId(userId);
    }

    @DeleteMapping("/{reviewId}")
    public Mono<ResponseEntity<Void>> deleteReview(@PathVariable String reviewId) {
            return reviewService.deleteReview(reviewId)
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                    .onErrorResume(NotFoundException.class, e -> Mono.just(new ResponseEntity<Void>(HttpStatus.NOT_FOUND)));
        }


    }
