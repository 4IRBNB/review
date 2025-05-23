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

    // [리뷰 작성]
    @Override
    @Transactional
    public ReviewInternalResponse addReview(CreateReviewInternalRequest request) {

        // 예약 서비스에서 유저 아이디로 해당 유저의 예약 정보 가져오기
        BaseResponse<List<ReservationResponse>> userReservations = reservationClient.getReservationById(
            request.userId());

        // 예약 정보 유효 검사
        Review review = null;
        UUID reservationId = null;
        boolean isValidReservation = false;

        for (ReservationResponse reservationResponse : userReservations.getData()) {
            if (reservationResponse.lodgeId().equals(request.lodgeId())
                && reservationResponse.reservationStatus().equals("COMPLETED")) {
                review = ReviewMapper.toReviewEntity(request, reservationResponse.id());
                reservationId = reservationResponse.id();
                isValidReservation = true;
                break;
            }
        }

        if (!isValidReservation) {
            throw new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND);
        }

        // 해당 예약 아이디로 작성된 리뷰 존재 여부 확인
        if (reviewRepository.existsByReservationId(reservationId)) {
            throw new CustomException(CustomExceptionCode.REVIEW_EXIST);
        }

        // 저장
        Review saved = reviewRepository.save(review);

        // Redis를 이용한 증분처리
        redisReviewCacheService.addRating(saved.getLodgeId(), saved.getRating());

        return ReviewMapper.toReviewResponse(saved);
    }

    // [리뷰 목록 조회]
    @Override
    public Page<ReviewInternalResponse> findReviewByLodgeId(UUID lodgeId, Pageable pageable) {

        // 숙소 ID로  리뷰 목록 조회
        Page<Review> reviewsByLodgeId = reviewRepository.findAllByLodgeId(lodgeId, pageable);

        // 리뷰가 없는 경우 예외처리
        if (reviewsByLodgeId.isEmpty()) {
            throw new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND);
        }

        return ReviewMapper.toReviewResponsePage(reviewsByLodgeId);
    }

    // [내 리뷰 조회]
    @Override
    public Page<ReviewInternalResponse> findMyReview(Long userId, Pageable pageable) {

        // 유저 ID로 리뷰 목록 조회
        Page<Review> reviewsByUserId = reviewRepository.findAllByUserId(userId, pageable);

        // 리뷰가 없는 경우 예외처리
        if (reviewsByUserId.isEmpty()) {
            throw new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND);
        }

        return ReviewMapper.toReviewResponsePage(reviewsByUserId);
    }

    // [리뷰 수정]
    @Override
    @Transactional
    public ReviewInternalResponse modifyReview(UUID reviewId, UpdateReviewInternalRequest request) {

        // 해당 리뷰가 없는 경우 예외처리
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND));

        // 리뷰 수정 및 별점 통계 증분 처리, 아이디 불일치시 예외처리
        Long beforeRating = review.getRating();

        if (Objects.equals(review.getUserId(), request.userId())) {
            review.updateReview(request.content(), request.rating());
            redisReviewCacheService.updateRating(review.getLodgeId(), beforeRating,
                review.getRating());
        } else {
            throw new CustomException(CustomExceptionCode.FORBIDDEN);
        }

        return ReviewMapper.toReviewResponse(review);
    }

    // [리뷰 삭제]
    @Override
    @Transactional
    public void removeReviewByUser(UUID reviewId, Long userId) {
        // 해당 리뷰가 없는 경우 예외처리
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND));

        // 아이디 일치 -> 리뷰 삭제 및 별점 증분처리, 불일치 -> 예외처리
        if (userId.equals(review.getUserId())) {
            review.setDeleted(userId, LocalDateTime.now());
            redisReviewCacheService.deleteRating(review.getLodgeId(), review.getRating());
        } else {
            throw new CustomException(CustomExceptionCode.FORBIDDEN);
        }
    }

    // [관리자 권한 리뷰 삭제]
    @Override
    @Transactional
    public void removeReviewByAdmin(UUID reviewId, Long userId) {

        // 해당 리뷰가 없는 경우 예외처리
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomExceptionCode.REVIEW_NOT_FOUND));

        // 리뷰 삭제
        review.setDeleted(userId, LocalDateTime.now());

        // 별점 증분처리
        redisReviewCacheService.deleteRating(review.getLodgeId(), review.getRating());
    }

    // [별점 통계]
    @Override
    @Transactional
    public RedisResponse getRatingStatistics(UUID lodgeId) {

        // 별점 통계 데이터 조회
        List<RatingInternalResponse> ratingStatistics = reviewRepository.findRatingStatistics(
            lodgeId);

        // 평균 별점 계산 로직 : 전체 별점 합계 / 전체 리뷰 갯수
        Long totalScore = 0L;
        for (int i = 0; i < ratingStatistics.size(); i++) {
            totalScore += (ratingStatistics.get(i).count() * ratingStatistics.get(i).rating());
        }

        Long totalReview = 0L;
        for (int i = 0; i < ratingStatistics.size(); i++) {
            totalReview += ratingStatistics.get(i).count();
        }

        double averageRating = totalScore.doubleValue() / totalReview.doubleValue();
        String averageRatingStr = String.format("%.2f", averageRating);

        // 평균 별점
        log.info("각 별점별 리뷰 갯수 : {}, 평균 별점 : {}", ratingStatistics, averageRatingStr);

//        Map<Object, Object> ratingCount = redisReviewCacheService.getRatingCountFromRedis(
//            lodgeId);
//        String totalScore = redisReviewCacheService.getTotalScoreFromRedis(lodgeId);
//        String totalReview = redisReviewCacheService.getTotalReviewFromRedis(lodgeId);

//        if (!redisReviewCacheService.isCache(lodgeId)) {
//            List<RatingInternalResponse> ratingInternalResponseList = reviewRepository.ratingStatistics(
//                lodgeId);
//            redisReviewCacheService.setDataToRedis(ratingInternalResponseList, lodgeId);
//        }
//        Map<Object, Object> ratingCount = redisReviewCacheService.getRatingCountFromRedis(
//            lodgeId);
//        String totalScore = redisReviewCacheService.getTotalScoreFromRedis(lodgeId);
//        String totalReview = redisReviewCacheService.getTotalReviewFromRedis(lodgeId);
//
//        log.info("캐싱정보 가져오기 : getRatingCount{}, totalScore: {}, totalReview: {}", ratingCount,
//            totalScore, totalReview);

        return RedisMapper.toRedisReviewResponse(ratingStatistics, averageRatingStr);
    }

}
