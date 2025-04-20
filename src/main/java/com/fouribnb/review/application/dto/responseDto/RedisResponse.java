package com.fouribnb.review.application.dto.responseDto;

import java.util.Map;
import lombok.Builder;

@Builder
public record RedisResponse(
    Map<?,?> ratingCount,
    Long totalScore,
    Long totalReview
) {

}
