package com.fouribnb.review.application.dto.requestDto;

import java.util.UUID;

public record CreateReviewInternalRequest(
    Long userId,
    UUID lodgeId,
    String content,
    Long rating
) {

}
