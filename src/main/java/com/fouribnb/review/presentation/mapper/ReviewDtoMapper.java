package com.fouribnb.review.presentation.mapper;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.presentation.dto.requestDto.CreateReviewRequest;
import com.fouribnb.review.presentation.dto.responseDto.ReviewResponse;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewDtoMapper {

  // 외부 Dto -> 내부 Dto
  public static CreateReviewInternalRequest toCreateInternalDto(CreateReviewRequest request) {
    return new CreateReviewInternalRequest(
        request.userId(),
        request.lodgeId(),
        request.content(),
        request.rating()
    );
  }

  // 내부 Dto -> 외부 Dto
  public static ReviewResponse toResponse(ReviewInternalResponse response) {
    return new ReviewResponse(
        response.reviewId(),
        response.userId(),
        response.lodgeId(),
        response.content(),
        response.rating()
    );
  }

  public static List<ReviewResponse> toResponseList(List<ReviewInternalResponse> internalResponseList) {
    return internalResponseList.stream()
        .map(ReviewDtoMapper::toResponse)
        .collect(Collectors.toList());
  }
}
