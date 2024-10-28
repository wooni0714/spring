package wooni.spring.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import wooni.spring.jwt.domain.Authority;
import wooni.spring.jwt.domain.Member;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record MemberDto(
        String username,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,
        String nickname,
        Set<AuthorityDto> authorityDtoSet
) {

    public static MemberDto from(Member member) {
        if (member == null) {
            return null;
        }

        return MemberDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .authorityDtoSet(member.getAuthorities().stream()
                        .map(authority -> new AuthorityDto(authority.getAuthorityName()))
                        .collect(Collectors.toSet()))
                .build();
    }

    public Member toMember() {
        Set<Authority> authorities = authorityDtoSet != null
                ? authorityDtoSet.stream()
                .map(dto -> Authority.builder().authorityName(dto.authorityName()).build())
                .collect(Collectors.toSet())
                : new HashSet<>();

        return Member.builder()
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .authorities(authorities)
                .build();
    }
}
