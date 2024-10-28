package wooni.spring.jwt.dto;

import lombok.*;

@Builder
public record TokenDto (
        String AccessToken,
        String RefreshToken
) {

}
