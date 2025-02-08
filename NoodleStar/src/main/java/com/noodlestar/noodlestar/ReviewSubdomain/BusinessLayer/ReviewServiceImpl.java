package com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.ReviewRepo;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewRequestModel;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewResponseModel;

import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishDescriptionException;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishNameException;
import com.noodlestar.noodlestar.utils.exceptions.InvalidDishPriceException;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;

    public ReviewServiceImpl(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public Flux<ReviewResponseModel> getAllReview() {
        return reviewRepo.findAll().map(EntityDTOUtil::toReviewResponseDTO);
    }

    @Override
    public Mono<ReviewResponseModel> addReview(Mono<ReviewRequestModel> reviewRequestModel) {
        return reviewRequestModel
                .map(request -> {
                    request.setDateSubmitted(LocalDateTime.now());
                    return EntityDTOUtil.toReviewEntity(request);
                })
                .flatMap(reviewRepo::insert)
                .flatMap(savedReview -> reviewRepo.findById(savedReview.getId()))
                .map(EntityDTOUtil::toReviewResponseDTO)
                .doOnSuccess(response -> log.info("Review added successfully with ID: {}", response.getReviewId()));
    }

    @Override
    public Flux<ReviewResponseModel> getReviewsByUserId(String userId) {
        return reviewRepo.findAllByUserId(userId).map(EntityDTOUtil::toReviewResponseDTO);
    }
    @Override
    public Mono<Void> deleteReview(String reviewId) {
        return reviewRepo.findReviewByReviewId(reviewId)
                .switchIfEmpty(Mono.error(new NotFoundException("Re with ID '" + reviewId + "' not found.")))
                .flatMap(reviewRepo::delete);
    }


}
