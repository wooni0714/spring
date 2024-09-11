package wooni.spring.data_redis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wooni.spring.data_redis.dto.UserInfoRequest;
import wooni.spring.data_redis.dto.UserInfoResponse;
import wooni.spring.data_redis.service.UserCacheService;
import wooni.spring.data_redis.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserCacheService userCacheService;

    @PostMapping("/save")
    public String saveUser(@RequestBody UserInfoRequest userInfoRequest) {
        return userService.save(userInfoRequest);
    }

    @GetMapping("/{id}")
    public UserInfoResponse getUser(@PathVariable String id) {
        return userCacheService.getUserById(id);
    }
}
