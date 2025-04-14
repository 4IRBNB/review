package com.fouribnb.review.presentation.dto.responseDto;

import java.util.UUID;

public record ReviewResponse(
    UUID reviewId,
    Long userId,
    UUID lodgeId,
    String content,
    Long rating
) {
}
