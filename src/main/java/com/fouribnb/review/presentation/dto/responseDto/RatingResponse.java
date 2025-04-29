package com.fouribnb.review.presentation.dto.responseDto;

import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record RatingResponse(

//    Map<?,?> ratingCount,
    List<?> ratingCount,
    Long totalScore,
    Long totalReview

) {

}
