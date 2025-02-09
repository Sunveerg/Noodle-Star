package com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer.MenuService;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuController;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer.ReviewService;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.Review;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerUnitTest {


    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        webTestClient = WebTestClient.bindToController(reviewController).build(); // Bind controller manually
    }

    @Test
    void getAllReview() {
        LocalDateTime fixedDate = LocalDateTime.of(2024, 12, 10, 18, 38, 9, 0);

        // Create mock review data with the fixed date
        ReviewResponseModel review2 = ReviewResponseModel.builder()
                .reviewId("1")
                .userId("23")
                .rating(4)
                .reviewerName("Jane Doe")
                .review("Very good experience")
                .dateSubmitted(fixedDate)  // Use the fixed date
                .build();

        ReviewResponseModel review3 = ReviewResponseModel.builder()
                .reviewId("2")
                .userId("24")
                .rating(3)
                .reviewerName("Jane Doe2")
                .review("Good experience")
                .dateSubmitted(fixedDate)  // Use the fixed date
                .build();


        // Mock the service to return the mock data
        when(reviewService.getAllReview()).thenReturn(Flux.just(review3, review2));

        // Perform the actual test using WebTestClient
        webTestClient.get()
                .uri("/api/v1/review")  // URI to the controller endpoint
                .accept(MediaType.TEXT_EVENT_STREAM)  // Accept JSON response
                .exchange()
                .expectStatus().isOk()  // Assert that the status is OK
                .expectBodyList(ReviewResponseModel.class)  // Assert that the response body is a list of MenuResponseModel
                .hasSize(2)  // Assert that the list has exactly 2 items
                .contains(
                        // Assert the first review, ignoring dateSubmitted field
                        new ReviewResponseModel("1", "23", 4, "Jane Doe", "Very good experience", fixedDate, false),
                        // Assert the second review, ignoring dateSubmitted field
                        new ReviewResponseModel("2", "24", 3, "Jane Doe2", "Good experience", fixedDate, false)
                );


        // Verify that the reviewService method was called once
        verify(reviewService, times(1)).getAllReview();
    }

    @Test
    void addReview() {
        LocalDateTime fixedDate = LocalDateTime.of(2024, 12, 10, 18, 38, 9, 0);

        // Create mock request and response data
        ReviewRequestModel requestModel = new ReviewRequestModel();
        requestModel.setReviewerName("John Doe");
        requestModel.setReview("Great experience");
        requestModel.setRating(5);

        ReviewResponseModel responseModel = ReviewResponseModel.builder()
                .reviewId("1")
                .rating(5)
                .reviewerName("John Doe")
                .review("Great experience")
                .dateSubmitted(fixedDate)
                .build();

        // Mock the service to return the mock response
        when(reviewService.addReview(any(Mono.class))).thenReturn(Mono.just(responseModel));

        // Perform the actual test using WebTestClient
        webTestClient.post()
                .uri("/api/v1/review")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestModel)
                .exchange()
                .expectStatus().isCreated()  // Assert that the status is CREATED
                .expectBody(ReviewResponseModel.class)
                .isEqualTo(responseModel);

        // Verify that the reviewService method was called once
        verify(reviewService, times(1)).addReview(any(Mono.class));
    }


    @Test
    void getReviewByUserId() {
        LocalDateTime fixedDate = LocalDateTime.of(2024, 12, 10, 18, 38, 9, 0);

        // Create mock review data
        ReviewResponseModel review1 = ReviewResponseModel.builder()
                .reviewId("1")
                .userId("23")
                .rating(4)
                .reviewerName("John Doe")
                .review("Very good experience")
                .dateSubmitted(fixedDate)
                .build();

        ReviewResponseModel review2 = ReviewResponseModel.builder()
                .reviewId("2")
                .userId("23")
                .rating(5)
                .reviewerName("Jane Doe")
                .review("Amazing service")
                .dateSubmitted(fixedDate)
                .build();

        // Mock the service to return the mock reviews for the userId "23"
        when(reviewService.getReviewsByUserId("23")).thenReturn(Flux.just(review1, review2));

        // Perform the actual test using WebTestClient
        webTestClient.get()
                .uri("/api/v1/review/23")  // URI to the controller endpoint with userId "23"
                .accept(MediaType.APPLICATION_JSON)  // Accept JSON response
                .exchange()
                .expectStatus().isOk()  // Assert that the status is OK
                .expectBodyList(ReviewResponseModel.class)  // Assert that the response body is a list of ReviewResponseModel
                .hasSize(2)  // Assert that the list has exactly 2 items
                .contains(
                        // Assert the first review
                        new ReviewResponseModel("1", "23", 4, "John Doe", "Very good experience", fixedDate, false),
                        // Assert the second review
                        new ReviewResponseModel("2", "23", 5, "Jane Doe", "Amazing service", fixedDate, false)
                );

        // Verify that the reviewService method was called once with the userId "23"
        verify(reviewService, times(1)).getReviewsByUserId("23");
    }
    @Test
    void deleteReview_Success() {
        when(reviewService.deleteReview("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/review/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(reviewService, times(1)).deleteReview("1");
    }

    @Test
    void deleteReview_NotFound() {
        when(reviewService.deleteReview("1")).thenReturn(Mono.error(new NotFoundException("Review not found")));

        webTestClient.delete()
                .uri("/api/v1/review/1")
                .exchange()
                .expectStatus().isNotFound();

        verify(reviewService, times(1)).deleteReview("1");
    }

    @Test
    void updateReview_Success() {
        String reviewId = "1";
        ReviewRequestModel requestModel = new ReviewRequestModel("user1", 5,"John Doe", "Updated review", LocalDateTime.now(), false);
        ReviewResponseModel responseModel = new ReviewResponseModel(reviewId, "23", 5, "John Doe", "Updated review", LocalDateTime.now(), false);

        when(reviewService.updateReview(any(Mono.class), eq(reviewId))).thenReturn(Mono.just(responseModel));

        webTestClient.put()
                .uri("/api/v1/review/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReviewResponseModel.class)
                .isEqualTo(responseModel);

        verify(reviewService, times(1)).updateReview(any(Mono.class), eq(reviewId));
    }

    @Test
    void updateReview_NotFound() {
        String reviewId = "1";
        ReviewRequestModel requestModel = new ReviewRequestModel("user1", 5,"John Doe", "Updated review", LocalDateTime.now(), false);

        when(reviewService.updateReview(any(Mono.class), eq(reviewId))).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/v1/review/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestModel)
                .exchange()
                .expectStatus().isNotFound();

        verify(reviewService, times(1)).updateReview(any(Mono.class), eq(reviewId));
    }

    @Test
    void getReviewById_Success() {
        String reviewId = "1";
        ReviewResponseModel responseModel = new ReviewResponseModel(reviewId, "23", 5, "John Doe", "Great review", LocalDateTime.now(), false);

        when(reviewService.getReviewById(reviewId)).thenReturn(Mono.just(responseModel));

        webTestClient.get()
                .uri("/api/v1/review/reviewId/" + reviewId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReviewResponseModel.class)
                .isEqualTo(responseModel);

        verify(reviewService, times(1)).getReviewById(reviewId);
    }

    @Test
    void getReviewById_NotFound() {
        String reviewId = "1";

        when(reviewService.getReviewById(reviewId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/review/reviewId/" + reviewId)
                .exchange()
                .expectStatus().isNotFound();

        verify(reviewService, times(1)).getReviewById(reviewId);
    }


}