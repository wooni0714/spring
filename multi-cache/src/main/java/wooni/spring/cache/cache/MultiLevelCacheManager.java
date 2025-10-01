package wooni.spring.cache.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.NonNull;

import java.util.Collection;

public record MultiLevelCacheManager(CacheManager caffeineCacheManager,
                                     CacheManager redisCacheManager) implements CacheManager {
    @Override
    public Cache getCache(@NonNull String name) {
        Cache caffeineCache = caffeineCacheManager.getCache(name);
        Cache redisCache = redisCacheManager.getCache(name);
        return new MultiLevelCache(caffeineCache, redisCache);
    }


    @Override
    @NonNull
    public Collection<String> getCacheNames() {
        return redisCacheManager.getCacheNames();
    }
}
