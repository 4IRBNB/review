package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.mapper.ReviewMapper;
import com.fouribnb.review.common.exception.CommonExceptionCode;
import com.fouribnb.review.common.exception.CustomException;
import com.fouribnb.review.domain.entity.Review;
import com.fouribnb.review.domain.repository.ReviewRepository;
import com.fourirbnb.common.config.JpaConfig;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        // TODO : 리뷰 서비스와 Feign Client 통신 - 해당 숙소에 사용 내역이 있는 유저만 작성가능

        Review saved = reviewRepository.save(review);

        return ReviewMapper.toResponse(saved);
    }

    @Override
    public Page<ReviewInternalResponse> getReviewsByLodgeId(UUID lodgeId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.getAllByLodgeId(lodgeId, pageable);

        if (reviewPage.isEmpty()) {
            throw new CustomException(CommonExceptionCode.REVIEW_NOT_FOUND);
        }

        Page<ReviewInternalResponse> internalResponsePage = ReviewMapper.toResponsePage(reviewPage);

        return internalResponsePage;
    }

    @Override
    public Page<ReviewInternalResponse> getAllByUserId(Long userId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.getAllByUserId(userId, pageable);

        if (reviewPage.isEmpty()) {
            throw new CustomException(CommonExceptionCode.REVIEW_NOT_FOUND);
        }

        Page<ReviewInternalResponse> internalResponsePage = ReviewMapper.toResponsePage(reviewPage);
        return internalResponsePage;
    }

    @Override
    @Transactional
    public ReviewInternalResponse updateReview(UUID reviewId, UpdateReviewInternalRequest request) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CommonExceptionCode.REVIEW_NOT_FOUND));

        if (Objects.equals(review.getUserId(), request.userId())) {
            review.updateReview(request.content(), request.rating());
        } else {
            throw new CustomException(CommonExceptionCode.FORBIDDEN);
        }

        return ReviewMapper.toResponse(review);
    }

    @Override
    @Transactional
    public void deleteReview(UUID reviewId, Long userId) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CommonExceptionCode.REVIEW_NOT_FOUND));

        if (Objects.equals(review.getUserId(), userId)) {
            review.setDeleted(userId, LocalDateTime.now());
        } else {
            throw new CustomException(CommonExceptionCode.FORBIDDEN);
        }

        log.info("After setDeleteReview, review: {}", review.getDeletedBy());
    }
}
