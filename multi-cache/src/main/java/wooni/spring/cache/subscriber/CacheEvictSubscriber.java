package wooni.spring.cache.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheEvictSubscriber implements MessageListener {
    private final CacheManager cacheManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String userId = new String(message.getBody());
        Objects.requireNonNull(cacheManager.getCache("users")).evict(userId);
        log.info("캐시 무효화: users {}", userId);
    }
}
