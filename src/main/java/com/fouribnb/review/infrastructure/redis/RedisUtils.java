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

    public void setRatingCount(String redisKey, String field, String value) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(redisKey, field, value);
        redisTemplate.expire(redisKey,5, TimeUnit.MINUTES);
        log.info("redisKey : {}, field : {}, value : {}", redisKey, field, value);
    }

    public void setData(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key,5, TimeUnit.MINUTES);
        log.info("setTotalScore key : {}, value : {}", key, value);
    }

    public Map<Object, Object> getRatingCount(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
