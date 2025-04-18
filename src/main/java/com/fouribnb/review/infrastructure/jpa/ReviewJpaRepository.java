package com.fouribnb.review.infrastructure.jpa;

import com.fouribnb.review.domain.entity.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, UUID> {

    List<Review> getAllByLodgeId(UUID lodgeId);

    List<Review> getAllByUserId(Long userId);
}
