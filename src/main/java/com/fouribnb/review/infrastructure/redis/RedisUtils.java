package com.fouribnb.review.infrastructure.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public void setHashData(String key, String field, String value) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, field, value);
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
    }

    public void setData(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
    }

    public Map<Object, Object> getHashData(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void addData(String ratingCountKey, String totalScoreKey, String totalReviewKey,
        Long rating) {
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                HashOperations<String, Object, Object> hashOperations = operations.opsForHash();
                hashOperations.increment(ratingCountKey, String.valueOf(rating), 1);

                ValueOperations<String, Object> valueOperations = operations.opsForValue();
                valueOperations.increment(totalScoreKey, rating);
                valueOperations.increment(totalReviewKey, 1L);

                return operations.exec();
            }
        });
    }
    public void updateData(String ratingCountKey, String totalScoreKey, Long beforeRating,
        Long afterRating) {
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                HashOperations<String, Object, Object> hashOperations = operations.opsForHash();

                hashOperations.increment(ratingCountKey, String.valueOf(beforeRating), -1);
                hashOperations.increment(ratingCountKey, String.valueOf(afterRating), 1);

                ValueOperations<String, Object> valueOperations = operations.opsForValue();
                if (afterRating > beforeRating) {
                    valueOperations.increment(totalScoreKey, (afterRating - beforeRating));
                } else if (beforeRating > afterRating) {
                    valueOperations.decrement(totalScoreKey, (beforeRating - afterRating));
                }

                return operations.exec();
            }
        });
    }

    public void deleteData(String ratingCountKey, String totalScoreKey, String totalReviewKey,
        Long rating) {
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                HashOperations<String, Object, Object> hashOperations = operations.opsForHash();
                hashOperations.increment(ratingCountKey, String.valueOf(rating), -1);

                ValueOperations<String, Object> valueOperations = operations.opsForValue();
                valueOperations.decrement(totalScoreKey, rating);
                valueOperations.decrement(totalReviewKey, 1L);

                return operations.exec();
            }
        });
    }
}
