package wooni.spring.data_redis.service;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import wooni.spring.data_redis.config.RedisService;
import wooni.spring.data_redis.dto.UserInfoRequest;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisService redisService;

    public String save(UserInfoRequest user) {
        redisService.setValues(user.id(), user.name());
        return "SUCCESS";
    }
}
