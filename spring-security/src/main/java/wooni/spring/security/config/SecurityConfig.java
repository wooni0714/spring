package wooni.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //권한 처리 애노테이션을 붙일 수 있게할지 말지에 관한 설정
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .formLogin(
                        login -> login
                        .loginPage("/login-view")
                        .loginProcessingUrl("/login") // 로그인 처리 URL
                        .usernameParameter("userId") // 사용자 ID 파라미터명
                        .passwordParameter("userPassword") // 비밀번호 파라미터명
                        .defaultSuccessUrl("/admin", true) // 로그인 성공 후 이동할 기본 URL
                        .failureUrl("/login-view?error=true") // 실패시 URL
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/login"))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PUBLIC_LIST).permitAll()
                        .requestMatchers("/admin").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }

    private final String[] PUBLIC_LIST = {
            "/home",
            "/user-login",
            "/login-view",
            "/join-view",
            "/user-join"
    };
}
