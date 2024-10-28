package wooni.spring.jwt.dto;

import lombok.*;

@Builder
public record AuthorityDto(
        String authorityName
) {

}