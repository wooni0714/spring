package wooni.spring.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import wooni.spring.cache.subscriber.CacheEvictSubscriber;

@Configuration
public class RedisMessageConfig {

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory, CacheEvictSubscriber subscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriber, new ChannelTopic("cacheEvictChannel"));
        return container;
    }
}
