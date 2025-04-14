package com.fouribnb.review.application.dto.requestDto;

import java.util.UUID;

public record UpdateReviewInternalRequest(
    Long userId,
    UUID lodgeId,
    String content,
    Long rating
) {

}
