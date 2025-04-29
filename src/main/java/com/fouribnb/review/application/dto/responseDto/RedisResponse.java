package com.fouribnb.review.application.dto.responseDto;

import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record RedisResponse(
//    Map<Object,Object> ratingCount,
    List<?> ratingCount,
    Long totalScore,
    Long totalReview
) {

}
