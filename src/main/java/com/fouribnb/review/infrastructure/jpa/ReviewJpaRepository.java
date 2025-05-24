package com.fouribnb.review.infrastructure.jpa;

import com.fouribnb.review.domain.entity.Review;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, UUID> {

    Page<Review> findAllByLodgeId(UUID lodgeId, Pageable pageable);

    Page<Review> findAllByUserId(Long userId, Pageable pageable);

    boolean existsByReservationId(UUID reservationId);
}
