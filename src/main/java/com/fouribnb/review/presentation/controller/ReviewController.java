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
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // [리뷰 작성]
    @PostMapping
    public BaseResponse<ReviewResponse> createReview(
        @Valid @RequestBody CreateReviewRequest request) {

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

    // [내 리뷰 조회]
    @GetMapping("/me")
    public BaseResponse<Page<ReviewResponse>> getReviewsByUser(@RequestHeader Long userId,
        @PageableDefault(
            size = 10,
            page = 0,
            direction = Direction.ASC
        ) Pageable pageable) {
        Page<ReviewInternalResponse> internalResponsePage = reviewService.getAllByUserId(userId,
            pageable);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponsePage(internalResponsePage),
            "내 리뷰 조회");
    }


    // [리뷰 수정]
    @PutMapping("/{reviewId}")
    public BaseResponse<ReviewResponse> updateReview(@PathVariable UUID reviewId,
        @Valid @RequestBody UpdateReviewRequest request) {

        UpdateReviewInternalRequest internalRequest = ReviewDtoMapper.toUpdateInternalDto(request);

        ReviewInternalResponse internalResponse = reviewService.updateReview(reviewId,
            internalRequest);

        return BaseResponse.SUCCESS(ReviewDtoMapper.toResponse(internalResponse),
            "리뷰 수정 성공");
    }

    // [리뷰 삭제]
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Object> deleteReview(@PathVariable UUID reviewId) {

        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
