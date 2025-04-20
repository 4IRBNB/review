package com.fouribnb.review.application.dto.responseDto;

import com.querydsl.core.annotations.QueryProjection;

public record RatingInternalResponse(
    Long rating,
    Long count
) {

    @QueryProjection
    public RatingInternalResponse {
    }
}
