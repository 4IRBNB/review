package com.fouribnb.review.presentation.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReviewResponse(
    UUID reviewId,
    Long userId,
    UUID lodgeId,
    String content,
    Long rating,
    Long deletedBy
) {
}
