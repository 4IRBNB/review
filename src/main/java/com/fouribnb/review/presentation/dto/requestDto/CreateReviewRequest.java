package com.fouribnb.review.presentation.dto.requestDto;

import java.util.UUID;

public record CreateReviewRequest(
    Long userId,
    UUID lodgeId,
    String content,
    Long rating
) {

}
