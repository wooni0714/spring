package wooni.spring.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wooni.spring.security.dto.MemberRequest;
import wooni.spring.security.service.JoinService;


@RestController
@RequiredArgsConstructor
public class MemberController {
    private final JoinService joinService;

    @PostMapping("/user-join")
    public String join(@RequestBody MemberRequest memberRequest) {
        joinService.join(memberRequest);
        return "redirect:/login";
    }
}