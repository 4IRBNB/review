package com.fouribnb.review.application.dto.requestDto;

public record UpdateReviewInternalRequest(
    Long userId,
    String content,
    Long rating
) {

}
