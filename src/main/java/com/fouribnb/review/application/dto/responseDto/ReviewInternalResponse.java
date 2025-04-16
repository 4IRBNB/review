package com.fouribnb.review.application.dto.responseDto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ReviewInternalResponse(
    UUID reviewId,
    Long userId,
    UUID lodgeId,
    String content,
    Long rating,
    Long deleteBy
    ) {

}
