package com.noodlestar.noodlestar.ReviewSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.ReviewRepo;
import com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer.ReviewResponseModel;

import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;

    public ReviewServiceImpl(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public Flux<ReviewResponseModel> getAllReview() {
        return reviewRepo.findAll().map(EntityDTOUtil::toReviewResponseDTO);
    }
}
