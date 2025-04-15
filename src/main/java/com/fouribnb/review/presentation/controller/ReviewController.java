package com.fouribnb.review.presentation.controller;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.requestDto.UpdateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.service.ReviewService;
import com.fouribnb.review.presentation.dto.requestDto.CreateReviewRequest;
import com.fouribnb.review.presentation.dto.requestDto.UpdateReviewRequest;
import com.fouribnb.review.presentation.dto.responseDto.ReviewResponse;
import com.fouribnb.review.presentation.mapper.ReviewDtoMapper;
import com.fourirbnb.common.response.BaseResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // [리뷰 작성]
    @PostMapping
    public BaseResponse<ReviewResponse> createReview(@RequestBody CreateReviewRequest request) {

        CreateReviewInternalRequest internalRequest = ReviewDtoMapper.toCreateInternalDto(request);

        ReviewInternalResponse internalResponse = reviewService.createReview(internalRequest);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponse(internalResponse), "리뷰작성 성공");
    }

    // [리뷰 목록 조회]
    @GetMapping("/lodge/{lodgeId}")
    public BaseResponse<List<ReviewResponse>> getReviewsByLodgeId(@PathVariable UUID lodgeId) {

        List<ReviewInternalResponse> internalResponse = reviewService.getReviewsByLodgeId(lodgeId);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponseList(internalResponse),
            "리뷰 목록 조회 성공");
    }

    // [리뷰 수정]
    @PutMapping("/{reviewId}")
    public BaseResponse<ReviewResponse> updateReview(@PathVariable UUID reviewId,
        @RequestBody UpdateReviewRequest request) {

        UpdateReviewInternalRequest internalRequest = ReviewDtoMapper.toUpdateInternalDto(request);

        ReviewInternalResponse internalResponse = reviewService.updateReview(reviewId,
            internalRequest);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponse(internalResponse),
            "리뷰 수정 성공");
    }
}
