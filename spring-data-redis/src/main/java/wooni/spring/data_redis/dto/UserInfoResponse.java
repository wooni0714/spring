package wooni.spring.data_redis.dto;


public record UserInfoResponse(
        String id,
        String name
) {

    public static UserInfoResponse of(String id, String name) {
        return new UserInfoResponse(id, name);
    }
}
