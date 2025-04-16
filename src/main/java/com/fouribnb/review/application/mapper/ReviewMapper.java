package com.fouribnb.review.application.mapper;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.domain.entity.Review;

public class ReviewMapper {

    // 내부 Dto -> Entity
    public static Review toEntity(CreateReviewInternalRequest request) {
        return new Review(
            request.userId(),
            request.lodgeId(),
            request.content(),
            request.rating()
        );
    }

    // Entity -> 내부 Dto
    public static ReviewInternalResponse toResponse(Review review) {
        ReviewInternalResponse internalResponse = ReviewInternalResponse.builder()
            .reviewId(review.getReviewId())
            .userId(review.getUserId())
            .lodgeId(review.getLodgeId())
            .content(review.getContent())
            .rating(review.getRating())
            .deleteBy(review.getDeletedBy())
            .build();
        return internalResponse;
    }
}
