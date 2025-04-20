package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.RatingInternalResponse;
import com.fouribnb.review.application.dto.responseDto.RedisResponse;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.mapper.RedisMapper;
import com.fouribnb.review.application.mapper.ReviewMapper;
import com.fouribnb.review.common.exception.CommonExceptionCode;
import com.fouribnb.review.common.exception.CustomException;
import com.fouribnb.review.domain.entity.Review;
import com.fouribnb.review.domain.repository.ReviewRepository;
import com.fouribnb.review.infrastructure.redis.RedisUtils;
import com.fourirbnb.common.config.JpaConfig;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
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
    private final RedisUtils redisUtils;

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

    @Override
    @Transactional
    public RedisResponse ratingStatistics(UUID lodgeId) {
        // 해당 lodgeId 의 리뷰 목록 조회( 별점 1 갯수 , 별점 2 갯수 ... ) 이런식으로 조회
        List<RatingInternalResponse> internalResponseList = reviewRepository.ratingStatistics(
            lodgeId);
        log.info("별점 , review: {}", internalResponseList);

        // 가져온 데이터 Redis 에 저장
        String redisKey1 = "ratingCount:"+lodgeId;
        for (RatingInternalResponse internalResponse : internalResponseList) {
            redisUtils.setRatingCount(redisKey1, internalResponse.rating().toString(),
                internalResponse.count().toString());
        }

        // 총 별점, 총 리뷰 수 Redis 에 저장
        String redisKey2 = "totalScore:"+lodgeId;
        Long totalScore = 0L;
        for (int i = 0; i < internalResponseList.size(); i++) {
            totalScore += (internalResponseList.get(i).count() * internalResponseList.get(i)
                .rating());
        }
        log.info("totalScore: {}", totalScore);
        redisUtils.setData(redisKey2, totalScore.toString());

        String redisKey3 = "totalReview:"+lodgeId;
        Long totalReview = 0L;
        for (int i = 0; i < internalResponseList.size(); i++) {
            totalReview += internalResponseList.get(i).count();
        }
        log.info("totalReview: {}", totalReview);
        redisUtils.setData(redisKey3, totalReview.toString());

        // response DTO 로 변환
        log.info("get 별점 : {}", redisUtils.getRatingCount(redisKey1));
        log.info("get 총 별점: {}", redisUtils.getData(redisKey2));
        log.info("get 리뷰 갯수: {}", redisUtils.getData(redisKey3));

        // TODO : 증분방식 적용

        return RedisMapper.toRedisResponse(redisUtils.getRatingCount(redisKey1),
            redisUtils.getData(redisKey2), redisUtils.getData(redisKey3));
    }
}
