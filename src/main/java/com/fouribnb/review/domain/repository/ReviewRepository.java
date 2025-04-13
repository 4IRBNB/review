package com.fouribnb.review.domain.repository;

import com.fouribnb.review.domain.entity.Review;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

}
