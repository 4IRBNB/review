package com.fouribnb.review.presentation.controller;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.service.ReviewService;
import com.fouribnb.review.presentation.dto.requestDto.CreateReviewRequest;
import com.fouribnb.review.presentation.dto.responseDto.ReviewResponse;
import com.fouribnb.review.presentation.mapper.ReviewDtoMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;


  @PostMapping
  public ReviewResponse createReview(@RequestBody CreateReviewRequest request) {

    CreateReviewInternalRequest internalRequest = ReviewDtoMapper.toCreateInternalDto(request);

    ReviewInternalResponse internalResponse = reviewService.createReview(internalRequest);

    return ReviewDtoMapper.toResponse(internalResponse);
  }

  @GetMapping("/lodge/{lodgeId}")
  public List<ReviewResponse> getReviewsByLodgeId(@PathVariable UUID lodgeId) {

    List<ReviewInternalResponse> internalResponse = reviewService.getReviewsByLodgeId(lodgeId);

    return ReviewDtoMapper.toResponseList(internalResponse);
  }
}
