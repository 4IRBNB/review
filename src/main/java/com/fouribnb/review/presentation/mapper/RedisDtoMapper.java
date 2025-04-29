package com.fouribnb.review.presentation.mapper;

import com.fouribnb.review.application.dto.responseDto.RedisResponse;
import com.fouribnb.review.presentation.dto.responseDto.RatingResponse;

public class RedisDtoMapper {

    // Redis -> 내부 DTO
//    public static RatingResponse toRatingResponse(RedisResponse redisResponse) {
//        RatingResponse ratingResponse = RatingResponse.builder()
//            .ratingCount(redisResponse.ratingCount())
//            .totalScore(redisResponse.totalScore())
//            .totalReview(redisResponse.totalReview())
//            .build();
//
//        return ratingResponse;
//    }
    public static RatingResponse toRatingResponse(RedisResponse redisResponse) {
        RatingResponse ratingResponse = RatingResponse.builder()
            .ratingCount(redisResponse.ratingCount())
            .totalScore(redisResponse.totalScore())
            .totalReview(redisResponse.totalReview())
            .build();

        return ratingResponse;
    }
}
