package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.mapper.ReviewMapper;
import com.fouribnb.review.domain.entity.Review;
import com.fouribnb.review.domain.repository.ReviewRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;

  @Override
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
}
