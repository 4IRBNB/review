package com.fouribnb.review.infrastructure.redis;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public void setHashData(String key, String field, String value) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, field, value);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    }

    public void setData(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    }

    public Map<Object, Object> getHashData(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void addHashData(String key, Long field) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.increment(key, String.valueOf(field), 1);
    }

    public void addData(String key, Long rating) {
        redisTemplate.opsForValue().increment(key, rating);

    }

    public void updateHashData(String key, Long beforeField, Long afterField) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.increment(key, String.valueOf(afterField), 1);
        hashOperations.increment(key, String.valueOf(beforeField), -1);
    }

    public void updateData(String key, Long afterRating, Long beforeRating) {
        if (afterRating > beforeRating) {
            redisTemplate.opsForValue().increment(key, (afterRating - beforeRating));
        } else if (beforeRating > afterRating) {
            redisTemplate.opsForValue().decrement(key, (beforeRating - afterRating));
        }
    }

    public void deleteHashData(String key, Long field) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.increment(key, String.valueOf(field), -1);
    }

    public void deleteData(String key, Long rating) {
        redisTemplate.opsForValue().decrement(key, rating);
    }
}
