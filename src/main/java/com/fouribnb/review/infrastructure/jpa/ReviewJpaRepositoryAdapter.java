package com.fouribnb.review.infrastructure.jpa;

import static com.fouribnb.review.domain.entity.QReview.review;

import com.fouribnb.review.application.dto.responseDto.RatingInternalResponse;
import com.fouribnb.review.domain.entity.Review;
import com.fouribnb.review.domain.repository.ReviewRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewJpaRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public Page<Review> getAllByLodgeId(UUID lodgeId, Pageable pageable) {
        return reviewJpaRepository.getAllByLodgeId(lodgeId, pageable);
    }

    @Override
    public Page<Review> getAllByUserId(Long userId, Pageable pageable) {
        return reviewJpaRepository.getAllByUserId(userId, pageable);
    }

    @Override
    public Optional<Review> findById(UUID reviewId) {
        return reviewJpaRepository.findById(reviewId);
    }

    @Override
    public List<RatingInternalResponse> ratingStatistics(UUID lodgeId) {
        return queryFactory
            .select(Projections.constructor(RatingInternalResponse.class,
                review.rating.as("rating"),
                review.count().as("count")))
            .from(review)
            .where(
                review.lodgeId.eq(lodgeId),
                review.deletedAt.isNull()
            )
            .groupBy(review.rating)
            .fetch();
    }
}
