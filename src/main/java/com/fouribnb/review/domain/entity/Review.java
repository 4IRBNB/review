package com.fouribnb.review.domain.entity;

import com.fourirbnb.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "p_review")
@DynamicUpdate
@SQLRestriction("deleted_at IS NULL")
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

    public void setDeleted(Long deletedBy, LocalDateTime deletedAt) {
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
    }

}
