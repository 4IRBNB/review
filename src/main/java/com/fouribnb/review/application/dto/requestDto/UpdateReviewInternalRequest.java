package com.fouribnb.review.application.dto.requestDto;

public record UpdateReviewInternalRequest(
    String content,
    Long rating
) {

}
