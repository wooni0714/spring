package wooni.spring.security.dto;

import lombok.Builder;
import wooni.spring.security.domain.Member;

@Builder
public record MemberRequest(
    String userId,
    String userPassword,
    String roles
) {
    public Member toMember() {
        return Member.builder()
                .userId(userId)
                .userPassword(userPassword)
                .roles(roles)
                .build();
    }
}