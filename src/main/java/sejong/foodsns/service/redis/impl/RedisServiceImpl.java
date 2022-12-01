package sejong.foodsns.service.redis.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.service.redis.RedisService;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public void setValues(String key, String value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    @Override
    @Transactional
    public void setValues(String key, String value, Duration duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    @Override
    public String getValues(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    @Override
    @Transactional
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
