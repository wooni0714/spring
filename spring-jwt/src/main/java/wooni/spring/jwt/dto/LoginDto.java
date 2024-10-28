package wooni.spring.jwt.dto;

import lombok.*;

@Builder
public record LoginDto (
        String username,
        String password
) {

}
