package com.noodlestar.noodlestar.ReviewSubdomain.DataLayer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    private String id;
    private String reviewId;
    private String userId;
    private int rating;
    private String reviewerName;
    private String review;
    private LocalDateTime dateSubmitted;

    @JsonProperty("isEdited")
    private boolean isEdited;

}
