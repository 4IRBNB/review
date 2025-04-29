package com.fouribnb.review.application.service;

import com.fouribnb.review.application.dto.responseDto.RatingInternalResponse;
import com.fouribnb.review.infrastructure.redis.RedisUtils;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisReviewCacheService {

    private final RedisUtils redisUtils;

    public void setDataToRedis(List<RatingInternalResponse> internalResponseList, UUID lodgeId) {
        String ratingCountKey = "ratingCount:" + lodgeId;
        for (RatingInternalResponse internalResponse : internalResponseList) {
            redisUtils.setHashData(ratingCountKey, internalResponse.rating().toString(),
                internalResponse.count().toString());
        }

        String totalScoreKey = "totalScore:" + lodgeId;
        Long totalScore = 0L;
        for (int i = 0; i < internalResponseList.size(); i++) {
            totalScore += (internalResponseList.get(i).count() * internalResponseList.get(i)
                .rating());
        }
        redisUtils.setData(totalScoreKey, totalScore.toString());

        String totalReviewKey = "totalReview:" + lodgeId;
        Long totalReview = 0L;
        for (int i = 0; i < internalResponseList.size(); i++) {
            totalReview += internalResponseList.get(i).count();
        }
        redisUtils.setData(totalReviewKey, totalReview.toString());
    }

    public Map<Object, Object> getRatingCountFromRedis(UUID lodgeId) {
        String ratingCountKey = "ratingCount:" + lodgeId;
        return redisUtils.getHashData(ratingCountKey);
    }

    public String getTotalScoreFromRedis(UUID lodgeId) {
        String totalScoreKey = "totalScore:" + lodgeId;
        return redisUtils.getData(totalScoreKey);
    }

    public String getTotalReviewFromRedis(UUID lodgeId) {
        String totalReviewKey = "totalReview:" + lodgeId;
        return redisUtils.getData(totalReviewKey);
    }

    public boolean isCache(UUID lodgeId) {
        return getTotalReviewFromRedis(lodgeId) != null;
    }


    public void addRating(UUID lodgeId, Long rating) {
        String ratingCountKey = "ratingCount:" + lodgeId;
        String totalScoreKey = "totalScore:" + lodgeId;
        String totalReviewKey = "totalReview:" + lodgeId;
        redisUtils.addData(ratingCountKey, totalScoreKey, totalReviewKey, rating);
    }

    public void updateRating(UUID lodgeId, Long beforeRating, Long afterRating) {
        String ratingCountKey = "ratingCount:" + lodgeId;
        String totalScoreKey = "totalScore:" + lodgeId;
        redisUtils.updateData(ratingCountKey, totalScoreKey, beforeRating, afterRating);
    }

    public void deleteRating(UUID lodgeId, Long rating) {
        String ratingCountKey = "ratingCount:" + lodgeId;
        String totalScoreKey = "totalScore:" + lodgeId;
        String totalReviewKey = "totalReview:" + lodgeId;
        redisUtils.deleteData(ratingCountKey, totalScoreKey, totalReviewKey, rating);
    }
}
