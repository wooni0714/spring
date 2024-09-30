package wooni.spring.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, name = "user_id")
    private String userId;

    @Column(name = "user_password")
    private String userPassword;

    private String roles;

    @Builder
    public Member(String userId, String userPassword, String roles) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.roles = roles;
    }

    public void updatePassword(String encodedPassword) {
        this.userPassword = encodedPassword;
    }
}