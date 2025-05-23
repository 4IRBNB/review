package com.fouribnb.review.application.mapper;

import com.fouribnb.review.application.dto.responseDto.RedisResponse;
import java.util.List;
import java.util.Map;

public class RedisMapper {

    // Redis -> Redis Response
//    public static RedisResponse toRedisResponse(Map<Object,Object> ratingCount, String totalScore,
//        String totalReview) {
//        RedisResponse redisResponse = RedisResponse.builder()
//            .ratingCount(ratingCount)
//            .totalScore(Long.valueOf(totalScore))
//            .totalReview(Long.valueOf(totalReview))
//            .build();
//
//        return redisResponse;
//    }
    public static RedisResponse toRedisReviewResponse(List<?> ratingCount, String averageRating) {
        RedisResponse redisResponse = RedisResponse.builder()
            .ratingCount(ratingCount)
            .averageRating(averageRating)
            .build();

        return redisResponse;
    }
}
