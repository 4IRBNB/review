package com.fouribnb.review.domain.repository;

import com.fouribnb.review.domain.entity.Review;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> getAllByLodgeId(UUID lodgeId);

    List<Review> getAllByUserId(Long userId);
}
