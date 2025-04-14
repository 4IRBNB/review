package com.fouribnb.review.application.dto.responseDto;

import java.util.UUID;

public record ReviewInternalResponse(
    UUID reviewId,
    Long userId,
    UUID lodgeId,
    String content,
    Long rating
    ) {

}
