package com.fouribnb.review.presentation.controller;

import com.fouribnb.review.application.service.ReviewService;
import com.fourirbnb.common.security.AuthenticatedUser;
import com.fourirbnb.common.security.RoleCheck;
import com.fourirbnb.common.security.UserInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/reviews")
@RequiredArgsConstructor
public class ReviewInternalController {

    private final ReviewService reviewService;

    // [관리자 권한 리뷰 삭제]
    @RoleCheck({"MANAGER", "MASTER"})
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Object> deleteReviewByAdmin(@PathVariable UUID reviewId,
        @AuthenticatedUser UserInfo user) {

        reviewService.removeReviewByAdmin(reviewId, user.getUserId());

        return ResponseEntity.noContent().build();
    }
}
