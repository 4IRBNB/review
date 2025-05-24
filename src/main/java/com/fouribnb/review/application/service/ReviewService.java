package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.RedisResponse;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    ReviewInternalResponse addReview(CreateReviewInternalRequest request);

    Page<ReviewInternalResponse> findReviewByLodgeId(UUID lodgeId,Pageable pageable);

    Page<ReviewInternalResponse> findMyReview(Long userId,Pageable pageable);

    ReviewInternalResponse modifyReview(UUID reviewId, UpdateReviewInternalRequest request);

    void removeReviewByUser(UUID reviewId, Long userId);

    void removeReviewByAdmin(UUID reviewId, Long userId);

    RedisResponse getRatingStatistics(UUID lodgeId);
}
