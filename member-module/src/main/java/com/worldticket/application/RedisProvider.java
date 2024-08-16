package com.worldticket.application;

import com.worldticket.infra.DataNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RedisProvider {
    private final RedisTemplate<String, String> redisTemplate;

    public String getData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 가져오는 메서드
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return Optional.ofNullable(valueOperations.get(key)).orElseThrow(DataNotFoundException::new);
    }

    public void setData(String key, String value) {//지정된 키(key)에 값을 저장하는 메서드
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    //지정된 키(key)에 값을 저장하고, 지정된 시간(duration) 후에 데이터가 만료되도록 설정하는 메서드
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 삭제하는 메서드
        redisTemplate.delete(key);
    }

}
