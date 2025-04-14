package com.fouribnb.review.presentation.dto.requestDto;

public record UpdateReviewRequest(
    String content,
    Long rating
) {

}
