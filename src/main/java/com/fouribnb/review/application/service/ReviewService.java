package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;

public interface ReviewService {

  ReviewInternalResponse createReview(CreateReviewInternalRequest request);
}
