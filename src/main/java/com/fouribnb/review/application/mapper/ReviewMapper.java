package com.fouribnb.review.application.mapper;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.domain.entity.Review;
import java.util.UUID;
import org.springframework.data.domain.Page;

public class ReviewMapper {

    // 내부 Dto -> Entity
    public static Review toEntity(CreateReviewInternalRequest request, UUID reservationId) {
        return new Review(
            request.userId(),
            request.lodgeId(),
            reservationId,
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
            .deletedBy(review.getDeletedBy())
            .build();
        return internalResponse;
    }

    public static Page<ReviewInternalResponse> toResponsePage(Page<Review> review) {
        Page<ReviewInternalResponse> internalResponsePage = review.map(r -> ReviewInternalResponse.builder()
            .reviewId(r.getReviewId())
            .lodgeId(r.getLodgeId())
            .userId(r.getUserId())
            .content(r.getContent())
            .rating(r.getRating())
            .deletedBy(r.getDeletedBy())
            .build());
        return internalResponsePage;
    }


}
