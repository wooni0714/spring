package wooni.spring.cache.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import wooni.spring.cache.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CacheIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CacheIntegrationTest.class);
    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testMultiLevelCache() {
        long start = System.currentTimeMillis();
        User user1 = userService.getUser(1L);
        long firstCall = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        User user2 = userService.getUser(1L);
        long secondCall = System.currentTimeMillis() - start;

        assertTrue(firstCall > 1500);
        assertTrue(secondCall < 100);
        log.info("First Call: {} ms", firstCall);
        log.info("Second Call: {} ms", secondCall);

        assertEquals(user1, user2);
    }

    @Test
    void testCacheEvictAndPubSub() {
        userService.getUser(2L);

        userService.updateUser(new User(2L, "User-2"));

        Cache cache = cacheManager.getCache("users");
        assertNotNull(cache);
        assertNull(cache.get(2L));
    }
}