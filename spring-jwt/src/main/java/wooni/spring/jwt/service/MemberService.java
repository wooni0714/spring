package wooni.spring.jwt.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooni.spring.jwt.config.jwt.JwtFilter;
import wooni.spring.jwt.config.jwt.TokenProvider;
import wooni.spring.jwt.domain.Authority;
import wooni.spring.jwt.domain.Member;
import wooni.spring.jwt.dto.LoginDto;
import wooni.spring.jwt.dto.MemberDto;
import wooni.spring.jwt.dto.TokenDto;
import wooni.spring.jwt.repository.UserRepository;
import wooni.spring.jwt.util.SecurityUtil;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void signUp(MemberDto memberDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(memberDto.username()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        String encodedPassword = passwordEncoder.encode(memberDto.password());

        Set<Authority> authorities = memberDto.authorityDtoSet() != null && !memberDto.authorityDtoSet().isEmpty()
                ? memberDto.toMember().getAuthorities()
                : Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Member member = memberDto.toMember()
                .toBuilder()
                .password(encodedPassword)
                .authorities(authorities)
                .activated(true)
                .build();

        userRepository.save(member);
    }

    public ResponseEntity<TokenDto> login(LoginDto loginDto) {
        try {
            //username, password 파라미터를 받아서 UsernamePasswordAuthenticationToken 객체를 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());

            //authenticationToken를 이용해서 authenticat 메소드 실행될때
            //CustomUserDetailsService -> loadUserByUsername 메소드 실행 되고
            //Authentication 객체를 생성 후 SecurityContext 저장
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = tokenProvider.createToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token);

            return new ResponseEntity<>(new TokenDto(token, refreshToken), httpHeaders, HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Transactional(readOnly = true)
    public MemberDto getUserWithAuthorities(String username) {
        return MemberDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberDto getMyUserWithAuthorities() {
        return MemberDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new RuntimeException("Member not found"))
        );
    }
}
