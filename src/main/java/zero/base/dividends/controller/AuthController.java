package zero.base.dividends.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zero.base.dividends.dto.AuthDto;
import zero.base.dividends.security.TokenProvider;
import zero.base.dividends.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup (@RequestBody AuthDto.SignUp request){
        //회원가입을 위한 API
        var result = this.memberService.register(request);
        return  ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AuthDto.SignIn request){
        //로그인을 위한 API
        var member = this.memberService.authenticate(request);
        var token = this.tokenProvider.generateToken(member.getUsername(),member.getRoles());
        return ResponseEntity.ok(token);
    }
}
