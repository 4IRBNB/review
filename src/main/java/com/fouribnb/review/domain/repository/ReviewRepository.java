package com.fouribnb.review.domain.repository;

import com.fouribnb.review.domain.entity.Review;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {

    Review save(Review review);

    List<Review> getAllByLodgeId(UUID lodgeId);

    List<Review> getAllByUserId(Long userId);

    Optional<Review> findById(UUID reviewId);
}
