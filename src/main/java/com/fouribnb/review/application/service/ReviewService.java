package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    ReviewInternalResponse createReview(CreateReviewInternalRequest request);

    List<ReviewInternalResponse> getReviewsByLodgeId(UUID lodgeId);

    Page<ReviewInternalResponse> getAllByUserId(Long userId,Pageable pageable);

    ReviewInternalResponse updateReview(UUID reviewId, UpdateReviewInternalRequest request);

    void deleteReview(UUID reviewId);

}
