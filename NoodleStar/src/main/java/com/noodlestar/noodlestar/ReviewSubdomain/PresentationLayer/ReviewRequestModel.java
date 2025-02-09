package com.noodlestar.noodlestar.ReviewSubdomain.PresentationLayer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestModel {

    private String userId;
    private int rating;
    private String reviewerName;
    private String review;
    private LocalDateTime dateSubmitted;

    @JsonProperty("isEdited")
    private boolean isEdited;
}
