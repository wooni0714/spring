package wooni.spring.data_redis.dto;

import lombok.Builder;

@Builder
public record UserInfoRequest(
        String id,
        String name
) {

}
