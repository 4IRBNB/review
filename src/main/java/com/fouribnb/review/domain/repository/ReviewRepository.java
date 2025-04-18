package com.fouribnb.review.domain.repository;

import com.fouribnb.review.domain.entity.Review;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepository {

    Review save(Review review);

    Page<Review> getAllByLodgeId(UUID lodgeId, Pageable pageable);

    Page<Review> getAllByUserId(Long userId, Pageable pageable);

    Optional<Review> findById(UUID reviewId);
}
