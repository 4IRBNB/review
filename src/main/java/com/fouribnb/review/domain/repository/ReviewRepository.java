package com.fouribnb.review.domain.repository;

import com.fouribnb.review.domain.entity.Review;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepository {

    Review save(Review review);

    List<Review> getAllByLodgeId(UUID lodgeId);

    Page<Review> getAllByUserId(Long userId, Pageable pageable);

    Optional<Review> findById(UUID reviewId);
}
