package wooni.spring.cache.cache;

import org.springframework.cache.Cache;
import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;

public record MultiLevelCache(Cache caffeineCache, Cache redisCache) implements Cache {

    @Override
    @NonNull
    public String getName() {
        return redisCache.getName();
    }

    @Override
    @NonNull
    public Object getNativeCache() {
        return redisCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(@NonNull Object key) {
        ValueWrapper value = caffeineCache.get(key);
        if (value != null) return value;
        value = redisCache.get(key);
        if (value != null) {
            caffeineCache.put(key, value.get()); // Redis에서 찾으면 Caffeine에 저장
        }
        return value;
    }

    @Override
    public <T> T get(@NonNull Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(@NonNull Object key, Object value) {
        caffeineCache.put(key, value);
        redisCache.put(key, value);
    }

    @Override
    public void evict(@NonNull Object key) {
        caffeineCache.evict(key);
        redisCache.evict(key);
    }

    @Override
    public void clear() {
        caffeineCache.clear();
        redisCache.clear();
    }
}
