package wooni.spring.cache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wooni.spring.cache.model.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RedisTemplate<String,Object> redisTemplate;

    @Cacheable(value = "users", key = "#id")
    public User getUser(Long id) {
        simulateSlowService();
        return new User(id, "User-" + id);
    }

    @CacheEvict(value = "users", key = "#user.id")
    public User updateUser(User user) {
        // DB 저장 로직 (예시)
        redisTemplate.convertAndSend("cacheEvictChannel", user.id());
        return user;
    }

    private void simulateSlowService() {
        try { Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
