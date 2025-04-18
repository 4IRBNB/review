package com.fouribnb.review.presentation.mapper;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.presentation.dto.requestDto.CreateReviewRequest;
import com.fouribnb.review.presentation.dto.requestDto.UpdateReviewRequest;
import com.fouribnb.review.presentation.dto.responseDto.ReviewResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class ReviewDtoMapper {

    // 외부 Dto -> 내부 Dto
    public static CreateReviewInternalRequest toCreateInternalDto(CreateReviewRequest request, Long userId) {
        return new CreateReviewInternalRequest(
            userId,
            request.lodgeId(),
            request.content(),
            request.rating()
        );
    }

    public static UpdateReviewInternalRequest toUpdateInternalDto(UpdateReviewRequest request, Long userId) {
        return new UpdateReviewInternalRequest(
            userId,
            request.content(),
            request.rating()
        );
    }

    // 내부 Dto -> 외부 Dto
    public static ReviewResponse toResponse(ReviewInternalResponse internalResponse) {
        ReviewResponse response = ReviewResponse.builder()
            .reviewId(internalResponse.reviewId())
            .userId(internalResponse.userId())
            .lodgeId(internalResponse.lodgeId())
            .content(internalResponse.content())
            .rating(internalResponse.rating())
            .deletedBy(internalResponse.deletedBy())
            .build();
        return response;
    }

    public static List<ReviewResponse> toResponseList(
        List<ReviewInternalResponse> internalResponseList) {
        return internalResponseList.stream()
            .map(ReviewDtoMapper::toResponse)
            .collect(Collectors.toList());
    }

    public static Page<ReviewResponse> toResponsePage(
        Page<ReviewInternalResponse> internalResponsePage) {
        Page<ReviewResponse> ResponsePage = internalResponsePage.map(r -> ReviewResponse.builder()
            .reviewId(r.reviewId())
            .lodgeId(r.lodgeId())
            .userId(r.userId())
            .content(r.content())
            .rating(r.rating())
            .deletedBy(r.deletedBy())
            .build());
        return ResponsePage;
    }

}
