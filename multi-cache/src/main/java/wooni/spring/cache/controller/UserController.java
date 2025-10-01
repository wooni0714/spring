package wooni.spring.cache.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wooni.spring.cache.model.User;
import wooni.spring.cache.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User updated = new User(id, user.name());
        return userService.updateUser(updated);
    }
}
