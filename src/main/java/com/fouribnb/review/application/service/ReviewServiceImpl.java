package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.RatingInternalResponse;
import com.fouribnb.review.application.dto.responseDto.RedisResponse;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.mapper.RedisMapper;
import com.fouribnb.review.application.mapper.ReviewMapper;
import com.fouribnb.review.common.exception.CustomExceptionCode;
import com.fouribnb.review.common.exception.CustomException;
import com.fouribnb.review.domain.entity.Review;
import com.fouribnb.review.domain.repository.ReviewRepository;
import com.fouribnb.review.infrastructure.client.ReservationClient;
import com.fouribnb.review.infrastructure.client.dto.ReservationResponse;
import com.fourirbnb.common.config.JpaConfig;
import com.fourirbnb.common.response.BaseResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private final RedisReviewCacheService redisReviewCacheService;
    private final ReservationClient reservationClient;

    @Override
    @Transactional
    public ReviewInternalResponse createReview(CreateReviewInternalRequest request) {

        BaseResponse<List<ReservationResponse>> reservationResponsesPage = reservationClient.getReservationById(
            request.userId());

        Review review = null;

        UUID reservationId = null;

        for (ReservationResponse reservationResponse : reservationResponsesPage.getData()) {
            if (reservationResponse.lodgeId().equals(request.lodgeId())
                && reservationResponse.reservationStatus().equals("COMPLETED")) {
                review = ReviewMapper.toEntity(request, reservationResponse.id());
                reservationId = reservationResponse.id();
                break;
            } else {
                throw new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND);
            }
        }

        log.info("해당 예약으로 작성된 리뷰 존재 : {}",reviewRepository.existsByReservationId(reservationId));

        if (reviewRepository.existsByReservationId(reservationId)) {
            throw new CustomException(CustomExceptionCode.REVIEW_EXIST);

        }
        Review saved = reviewRepository.save(review);

        return ReviewMapper.toResponse(saved);
    }

    @Override
    public Page<ReviewInternalResponse> getReviewsByLodgeId(UUID lodgeId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.getAllByLodgeId(lodgeId, pageable);

        if (reviewPage.isEmpty()) {
            throw new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND);
        }

        Page<ReviewInternalResponse> internalResponsePage = ReviewMapper.toResponsePage(reviewPage);

        return internalResponsePage;
    }

    @Override
    public Page<ReviewInternalResponse> getAllByUserId(Long userId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.getAllByUserId(userId, pageable);

        if (reviewPage.isEmpty()) {
            throw new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND);
        }

        Page<ReviewInternalResponse> internalResponsePage = ReviewMapper.toResponsePage(reviewPage);
        return internalResponsePage;
    }

    @Override
    @Transactional
    public ReviewInternalResponse updateReview(UUID reviewId, UpdateReviewInternalRequest request) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND));

        if (Objects.equals(review.getUserId(), request.userId())) {
            review.updateReview(request.content(), request.rating());
        } else {
            throw new CustomException(CustomExceptionCode.FORBIDDEN);
        }

        return ReviewMapper.toResponse(review);
    }

    @Override
    @Transactional
    public void deleteReviewByUser(UUID reviewId, Long userId) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND));

        if (userId.equals(review.getUserId())) {
            review.setDeleted(userId, LocalDateTime.now());
        } else {
            throw new CustomException(CustomExceptionCode.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public void deleteReviewByAdmin(UUID reviewId, Long userId) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND));

        review.setDeleted(userId, LocalDateTime.now());
    }

    @Override
    @Transactional
    public RedisResponse ratingStatistics(UUID lodgeId) {

        if (redisReviewCacheService.isCache(lodgeId)) {
            log.info("캐싱 처리");
            List<RatingInternalResponse> ratingInternalResponseList = reviewRepository.ratingStatistics(
                lodgeId);
            redisReviewCacheService.setDataToRedis(ratingInternalResponseList, lodgeId);
        }
        Map<Object, Object> ratingCount = redisReviewCacheService.getRatingCountFromRedis(
            lodgeId);
        String totalScore = redisReviewCacheService.getTotalScoreFromRedis(lodgeId);
        String totalReview = redisReviewCacheService.getTotalReviewFromRedis(lodgeId);

        log.info("캐싱정보 가져오기 : getRatingCount, totalScore: {}, totalReview: {}", ratingCount,
            totalScore);
        // TTL 로 데이터 일관성 관리 -> 증분방식 적용하여 데이터 일관성 관리
        return RedisMapper.toRedisResponse(ratingCount, totalScore, totalReview);
    }


}
