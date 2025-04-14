package com.fouribnb.review.domain.entity;

import com.fourirbnb.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id", nullable = false)
    private UUID reviewId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "lodge_id", nullable = false)
    private UUID lodgeId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "rating", nullable = false)
    private Long rating;

    @Builder
    public Review(Long userId, UUID lodgeId, String content, Long rating) {
        this.userId = userId;
        this.lodgeId = lodgeId;
        this.content = content;
        this.rating = rating;
    }

    public void updateReview(String content, Long rating) {
        this.content = content;
        this.rating = rating;
    }

}
