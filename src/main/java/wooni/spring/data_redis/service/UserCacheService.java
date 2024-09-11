package wooni.spring.data_redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wooni.spring.data_redis.config.RedisService;
import wooni.spring.data_redis.dto.UserInfoRequest;
import wooni.spring.data_redis.dto.UserInfoResponse;


@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final RedisService redisService;

    @Cacheable(value = "userId", key = "#id", cacheManager = "cacheManager")
    public UserInfoResponse getUserById(String id) {
        String name = redisService.getValues(id);

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return new UserInfoResponse(id, name);
    }
}
