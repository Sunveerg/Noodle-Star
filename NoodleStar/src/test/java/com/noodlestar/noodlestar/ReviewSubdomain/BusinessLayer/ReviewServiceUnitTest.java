package com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.Review;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.ReviewRepo;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewRequestModel;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewResponseModel;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
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


    @Test
    public void whenGetReviewsByUserId_thenReturnReviewsForThatUser() {
        // Arrange
        String userId = "user-123";

        Review review1 = Review.builder()
                .id(UUID.randomUUID().toString())
                .reviewId(UUID.randomUUID().toString())
                .userId(userId)
                .rating(5)
                .reviewerName("John Doe")
                .review("Excellent service")
                .dateSubmitted(LocalDateTime.now())
                .build();

        Review review2 = Review.builder()
                .id(UUID.randomUUID().toString())
                .reviewId(UUID.randomUUID().toString())
                .userId(userId)
                .rating(4)
                .reviewerName("Jane Doe")
                .review("Very good experience")
                .dateSubmitted(LocalDateTime.now())
                .build();

        when(reviewRepository.findAllByUserId(userId)).thenReturn(Flux.just(review1, review2));

        // Act
        Flux<ReviewResponseModel> result = reviewService.getReviewsByUserId(userId);

        // Assert
        StepVerifier
                .create(result)
                .expectNextMatches(response -> response.getReviewId().equals(review1.getReviewId()) &&
                        response.getReviewerName().equals("John Doe") &&
                        response.getUserId().equals(userId))
                .expectNextMatches(response -> response.getReviewId().equals(review2.getReviewId()) &&
                        response.getReviewerName().equals("Jane Doe") &&
                        response.getUserId().equals(userId))
                .verifyComplete();

        verify(reviewRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    public void whenDeleteReview_thenReviewIsDeleted() {
        String reviewId = review1.getReviewId();
        when(reviewRepository.findReviewByReviewId(reviewId)).thenReturn(Mono.just(review1));
        when(reviewRepository.delete(review1)).thenReturn(Mono.empty());

        Mono<Void> result = reviewService.deleteReview(reviewId);

        StepVerifier
                .create(result)
                .verifyComplete();

        verify(reviewRepository, times(1)).findReviewByReviewId(reviewId);
        verify(reviewRepository, times(1)).delete(review1);
    }

    @Test
    public void whenDeleteNonExistentReview_thenNotFoundExceptionIsThrown() {
        String reviewId = UUID.randomUUID().toString();
        when(reviewRepository.findReviewByReviewId(reviewId)).thenReturn(Mono.empty());

        Mono<Void> result = reviewService.deleteReview(reviewId);

        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Re with ID '" + reviewId + "' not found."))
                .verify();

        verify(reviewRepository, times(1)).findReviewByReviewId(reviewId);
        verify(reviewRepository, never()).delete(any());
    }

    @Test
    public void whenUpdateReview_thenReturnUpdatedReview() {
        // Arrange
        String reviewId = review1.getReviewId();
        ReviewRequestModel requestModel = new ReviewRequestModel();
        requestModel.setRating(3);
        requestModel.setReview("Good but could be better");

        Review updatedReview = Review.builder()
                .id(review1.getId())
                .reviewId(review1.getReviewId())
                .rating(requestModel.getRating())
                .review(requestModel.getReview())
                .reviewerName(review1.getReviewerName())
                .dateSubmitted(LocalDateTime.now())
                .isEdited(true)
                .build();

        when(reviewRepository.findReviewByReviewId(reviewId)).thenReturn(Mono.just(review1));
        when(reviewRepository.save(any(Review.class))).thenReturn(Mono.just(updatedReview));

        // Act
        Mono<ReviewResponseModel> result = reviewService.updateReview(Mono.just(requestModel), reviewId);

        // Assert
        StepVerifier
                .create(result)
                .expectNextMatches(response -> response.getRating() == 3 &&
                        response.getReview().equals("Good but could be better") &&
                        response.isEdited())
                .verifyComplete();

        verify(reviewRepository, times(1)).findReviewByReviewId(reviewId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void whenUpdateNonExistentReview_thenReturnNotFoundException() {
        // Arrange
        String reviewId = UUID.randomUUID().toString();
        ReviewRequestModel requestModel = new ReviewRequestModel();
        requestModel.setRating(3);
        requestModel.setReview("Good but could be better");

        when(reviewRepository.findReviewByReviewId(reviewId)).thenReturn(Mono.empty());

        // Act
        Mono<ReviewResponseModel> result = reviewService.updateReview(Mono.just(requestModel), reviewId);

        // Assert
        StepVerifier
                .create(result)
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Review not found with id: " + reviewId))
                .verify();

        verify(reviewRepository, times(1)).findReviewByReviewId(reviewId);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    public void whenGetReviewById_thenReturnReview() {
        // Arrange
        String reviewId = review1.getReviewId();
        when(reviewRepository.findReviewByReviewId(reviewId)).thenReturn(Mono.just(review1));

        // Act
        Mono<ReviewResponseModel> result = reviewService.getReviewById(reviewId);

        // Assert
        StepVerifier
                .create(result)
                .expectNextMatches(response -> response.getReviewId().equals(review1.getReviewId()) &&
                        response.getReviewerName().equals(review1.getReviewerName()))
                .verifyComplete();

        verify(reviewRepository, times(1)).findReviewByReviewId(reviewId);
    }

    @Test
    public void whenGetNonExistentReviewById_thenReturnNotFoundException() {
        // Arrange
        String reviewId = UUID.randomUUID().toString();
        when(reviewRepository.findReviewByReviewId(reviewId)).thenReturn(Mono.empty());

        // Act
        Mono<ReviewResponseModel> result = reviewService.getReviewById(reviewId);

        // Assert
        StepVerifier
                .create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(reviewRepository, times(1)).findReviewByReviewId(reviewId);
    }


}