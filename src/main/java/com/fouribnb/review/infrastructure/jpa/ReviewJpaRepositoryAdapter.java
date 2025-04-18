package com.fouribnb.review.infrastructure.jpa;

import com.fouribnb.review.domain.entity.Review;
import com.fouribnb.review.domain.repository.ReviewRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewJpaRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public List<Review> getAllByLodgeId(UUID lodgeId) {
        return reviewJpaRepository.getAllByLodgeId(lodgeId);
    }

    @Override
    public List<Review> getAllByUserId(Long userId) {
        return reviewJpaRepository.getAllByUserId(userId);
    }

    @Override
    public Optional<Review> findById(UUID reviewId) {
        return reviewJpaRepository.findById(reviewId);
    }
}
