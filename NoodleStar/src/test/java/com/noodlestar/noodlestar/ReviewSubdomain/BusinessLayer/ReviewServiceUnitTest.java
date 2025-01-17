package com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.Review;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.ReviewRepo;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewRequestModel;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewResponseModel;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceUnitTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepo reviewRepository;

    Review review1 = Review.builder()
            .id(UUID.randomUUID().toString())
            .reviewId(UUID.randomUUID().toString())
            .rating(5)
            .reviewerName("John Doe")
            .review("Excellent service")
            .dateSubmitted(LocalDateTime.now())
            .build();

    Review review2 = Review.builder()
            .id(UUID.randomUUID().toString())
            .reviewId(UUID.randomUUID().toString())
            .rating(4)
            .reviewerName("Jane Doe")
            .review("Very good experience")
            .dateSubmitted(LocalDateTime.now())
            .build();



    @Test
    public void whenGetAllReviews_thenReturnReviews() {
        // Arrange
        when(reviewRepository.findAll()).thenReturn(Flux.just(review1, review2));

        // Act
        Flux<ReviewResponseModel> result = reviewService.getAllReview();

        // Assert
        StepVerifier
                .create(result)
                .expectNextMatches(reviewResponseDTO -> reviewResponseDTO.getReviewId().equals(review1.getReviewId()))
                .expectNextMatches(reviewResponseDTO -> reviewResponseDTO.getReviewId().equals(review2.getReviewId()))
                .verifyComplete();
    }

    @Test
    public void whenAddReview_thenReviewIsSavedAndReturned() {
        // Arrange
        String id = UUID.randomUUID().toString();
        ReviewRequestModel requestModel = new ReviewRequestModel();
        requestModel.setReviewerName("John Doe");
        requestModel.setReview("Great experience");
        requestModel.setRating(5);

        Review reviewEntity = Review.builder()
                .id(id)
                .reviewId(UUID.randomUUID().toString())
                .reviewerName(requestModel.getReviewerName())
                .review(requestModel.getReview())
                .rating(requestModel.getRating())
                .dateSubmitted(LocalDateTime.now())
                .build();

        ReviewResponseModel responseModel = EntityDTOUtil.toReviewResponseDTO(reviewEntity);

        when(reviewRepository.insert(any(Review.class))).thenReturn(Mono.just(reviewEntity));
        when(reviewRepository.findById(id)).thenReturn(Mono.just(reviewEntity));

        // Act
        Mono<ReviewResponseModel> result = reviewService.addReview(Mono.just(requestModel));

        // Assert
        StepVerifier
                .create(result)
                .expectNextMatches(response -> response.getReviewerName().equals("John Doe") &&
                        response.getReview().equals("Great experience") &&
                        response.getRating() == 5)
                .verifyComplete();

        verify(reviewRepository, times(1)).insert(any(Review.class));
        verify(reviewRepository, times(1)).findById(id);
    }

    @Test
    public void whenAddReviewFails_thenErrorIsReturned() {
        // Arrange
        ReviewRequestModel requestModel = new ReviewRequestModel();
        requestModel.setReviewerName("John Doe");
        requestModel.setReview("Great experience");
        requestModel.setRating(5);

        when(reviewRepository.insert(any(Review.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act
        Mono<ReviewResponseModel> result = reviewService.addReview(Mono.just(requestModel));

        // Assert
        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();

        verify(reviewRepository, times(1)).insert(any(Review.class));
        verify(reviewRepository, never()).findById(anyString());
    }

}