package com.fouribnb.review.presentation.controller;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.RedisResponse;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.service.ReviewService;
import com.fouribnb.review.presentation.dto.requestDto.CreateReviewRequest;
import com.fouribnb.review.presentation.dto.requestDto.UpdateReviewRequest;
import com.fouribnb.review.presentation.dto.responseDto.RatingResponse;
import com.fouribnb.review.presentation.dto.responseDto.ReviewResponse;
import com.fouribnb.review.presentation.mapper.RedisDtoMapper;
import com.fouribnb.review.presentation.mapper.ReviewDtoMapper;
import com.fourirbnb.common.response.BaseResponse;
import com.fourirbnb.common.security.AuthenticatedUser;
import com.fourirbnb.common.security.UserInfo;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // [리뷰 작성]
    // TODO : 로그인한 사용자가 CUSTOMER 권한을 가져야 리뷰작성 가능
    @PostMapping
    public BaseResponse<ReviewResponse> createReview(
        @Valid @RequestBody CreateReviewRequest request, @AuthenticatedUser UserInfo user) {
        CreateReviewInternalRequest internalRequest = ReviewDtoMapper.toCreateInternalDto(request,
            user.getUserId());

        ReviewInternalResponse internalResponse = reviewService.createReview(internalRequest);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponse(internalResponse), "리뷰 작성 성공");
    }

    // [리뷰 목록 조회]
    @GetMapping("/lodge/{lodgeId}")
    public BaseResponse<Page<ReviewResponse>> getReviewsByLodgeId(@PathVariable UUID lodgeId,
        @PageableDefault(
            size = 10,
            page = 0,
            direction = Direction.ASC
        ) Pageable pageable) {

        Page<ReviewInternalResponse> internalResponse = reviewService.getReviewsByLodgeId(lodgeId,
            pageable);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponsePage(internalResponse),
            "리뷰 목록 조회 성공");
    }

    // [내 리뷰 조회]
    @GetMapping("/me")
    public BaseResponse<Page<ReviewResponse>> getReviewsByUser(@AuthenticatedUser UserInfo user,
        @PageableDefault(
            size = 10,
            page = 0
        ) Pageable pageable) {
        Page<ReviewInternalResponse> internalResponsePage = reviewService.getAllByUserId(user.getUserId(),
            pageable);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponsePage(internalResponsePage),
            "내 리뷰 조회");
    }


    // [리뷰 수정]
    // TODO : 로그인한 사용자가 CUSTOMER 권한을 가져야 리뷰수정 가능
    @PutMapping("/{reviewId}")
    public BaseResponse<ReviewResponse> updateReview(@PathVariable UUID reviewId,
        @Valid @RequestBody UpdateReviewRequest request, @AuthenticatedUser UserInfo user) {

        UpdateReviewInternalRequest internalRequest = ReviewDtoMapper.toUpdateInternalDto(request,
            user.getUserId());

        ReviewInternalResponse internalResponse = reviewService.updateReview(reviewId,
            internalRequest);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponse(internalResponse),
            "리뷰 수정 성공");
    }

    // [리뷰 삭제]
    // TODO : 로그인한 사용자가 CUSTOMER 권한을 가져야 리뷰삭제 가능
    @DeleteMapping("/{reviewId}")
    public BaseResponse<Object> deleteReview(@PathVariable UUID reviewId,
        @AuthenticatedUser UserInfo user) {

        reviewService.deleteReview(reviewId, user.getUserId());

        return BaseResponse.SUCCESS(null, "리뷰 삭제 성공", 204);
    }

    // [별점 통계]
    @GetMapping("/lodge/{lodgeId}/statistics")
    public BaseResponse<RatingResponse> ratingStatistics(@PathVariable UUID lodgeId) {

        RedisResponse redisResponse = reviewService.ratingStatistics(lodgeId);

        return BaseResponse.SUCCESS(RedisDtoMapper.toRatingResponse(redisResponse), "별점 통계 성공");
    }
}
