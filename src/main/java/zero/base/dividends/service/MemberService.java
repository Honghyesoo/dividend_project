package zero.base.dividends.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zero.base.dividends.domain.MemberEntity;
import zero.base.dividends.dto.AuthDto;
import zero.base.dividends.repository.MemberRepository;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    //회원가입
    public MemberEntity register(AuthDto.SignUp member){
        //중복된 아이디가 없는지 확인
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (!exists){
            throw new RuntimeException("이미 사용중인 아이디 입니다.");
        }
        //중복된 아이디가 없다면 해당 id의 password 인코딩한 값을 레파지토리에 저장
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());

        return result;
    }

    //로그인
    public MemberEntity authenticate(AuthDto.SignIn member){
        return null;
    }
}
