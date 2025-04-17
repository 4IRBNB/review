package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.mapper.ReviewMapper;
import com.fouribnb.review.domain.entity.Review;
import com.fouribnb.review.domain.repository.ReviewRepository;
import com.fourirbnb.common.config.JpaConfig;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Import(JpaConfig.class)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public ReviewInternalResponse createReview(CreateReviewInternalRequest request) {

        Review review = ReviewMapper.toEntity(request);
        Review saved = reviewRepository.save(review);

        return ReviewMapper.toResponse(saved);
    }

    @Override
    public List<ReviewInternalResponse> getReviewsByLodgeId(UUID lodgeId) {
        return reviewRepository.getAllByLodgeId(lodgeId).stream()
            .map(ReviewMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewInternalResponse updateReview(UUID reviewId, UpdateReviewInternalRequest request) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 리뷰가 없습니다."));

        review.updateReview(request.content(), request.rating());

        return ReviewMapper.toResponse(review);
    }

    @Override
    @Transactional
    public void deleteReview(UUID reviewId) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 리뷰가 없습니다."));
        log.info("Before setDeleteReview, review: {}", review.getDeletedBy());
        review.setDeleted(10L, LocalDateTime.now());
        log.info("After setDeleteReview, review: {}", review.getDeletedBy());
    }
}
