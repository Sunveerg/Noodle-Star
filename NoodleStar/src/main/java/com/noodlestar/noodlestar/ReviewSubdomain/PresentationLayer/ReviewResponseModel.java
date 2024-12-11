package com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseModel {
    private String reviewId;
    private int rating;
    private String reviewerName;
    private String review;
    private LocalDateTime dateSubmitted;
}
