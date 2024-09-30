package wooni.spring.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooni.spring.security.domain.Member;
import wooni.spring.security.dto.MemberRequest;
import wooni.spring.security.repository.MemberRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class JoinService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(MemberRequest memberRequest) {
        Member member = memberRequest.toMember();
        String encodedPassword = passwordEncoder.encode(member.getUserPassword());
        member.updatePassword(encodedPassword);
        memberRepository.save(member);
    }
}
