package com.fouribnb.review.presentation.dto.requestDto;

import java.util.UUID;

public record UpdateReviewRequest(
    Long userId,
    UUID lodgeId,
    String content,
    Long rating
) {

}
