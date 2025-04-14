package com.fouribnb.review.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.fouribnb.review.application.dto.requestDto.CreateReviewInternalRequest;
import com.fouribnb.review.application.dto.responseDto.ReviewInternalResponse;
import com.fouribnb.review.application.mapper.ReviewMapper;
import com.fouribnb.review.domain.entity.Review;
import com.fouribnb.review.domain.repository.ReviewRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  private static final Logger log = LoggerFactory.getLogger(ReviewServiceImplTest.class);
  @Mock
  ReviewRepository reviewRepository;

  @InjectMocks
  private ReviewServiceImpl reviewService;

  private CreateReviewInternalRequest request;

  @BeforeEach
  void setUp() {
    request = new CreateReviewInternalRequest(
        1L,
        UUID.randomUUID(),
        "숙소가 깔끔합니다!",
        5L
    );
  }

  @Nested
  @DisplayName("리뷰 작성 테스트")
  class createReview {

    @Test
    @DisplayName("리뷰 작성 테스트 - 성공")
    void success() {
      // given
      Review savedReview = Review.builder()
          .userId(request.userId())
          .lodgeId(request.lodgeId())
          .content(request.content())
          .rating(request.rating())
          .build();

      when(reviewRepository.save(any(Review.class)))
          .thenReturn(savedReview);

      // when
      ReviewInternalResponse response = reviewService.createReview(request);

      // then
      assertThat(response.userId()).isEqualTo(request.userId());
      assertThat(response.lodgeId()).isEqualTo(request.lodgeId());
      assertThat(response.content()).isEqualTo(request.content());
      assertThat(response.rating()).isEqualTo(request.rating());
    }
  }

  @Nested
  @DisplayName("리뷰 목록 테스트")
  class getReviews {

    @Test
    @DisplayName("리뷰 목록 테스트 - 성공")
    void success() {
      // given
      Review savedReview = Review.builder()
          .userId(request.userId())
          .lodgeId(request.lodgeId())
          .content(request.content())
          .rating(request.rating())
          .build();

      UUID reviewId = UUID.randomUUID();
      ReflectionTestUtils.setField(savedReview, "reviewId", reviewId);

      ReviewInternalResponse internalResponse = new ReviewInternalResponse(
          savedReview.getReviewId(),
          savedReview.getUserId(),
          savedReview.getLodgeId(),
          savedReview.getContent(),
          savedReview.getRating()
      );

      when(reviewRepository.getAllByLodgeId(request.lodgeId())).thenReturn(List.of(savedReview));

      // static 메서드 mock 처리
      try (MockedStatic<ReviewMapper> reviewMapperStatic = mockStatic(ReviewMapper.class)) {
        reviewMapperStatic
            .when(() -> ReviewMapper.toResponse(savedReview))
            .thenReturn(internalResponse);

        // when
        List<ReviewInternalResponse> result = reviewService.getReviewsByLodgeId(request.lodgeId());

        // then
        assertThat(result).hasSize(1);
        ReviewInternalResponse response = result.get(0);
        assertThat(response.reviewId()).isEqualTo(reviewId);
        assertThat(response.userId()).isEqualTo(request.userId());
        assertThat(response.lodgeId()).isEqualTo(request.lodgeId());
        assertThat(response.content()).isEqualTo(request.content());
        assertThat(response.rating()).isEqualTo(request.rating());
      }
    }
  }
}