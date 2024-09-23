package zero.base.dividends.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zero.base.dividends.service.MemberService;


import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    private static final String KEY_ROLES = "roles";
    private final MemberService memberService;

    // HS512를 위한 충분히 강력한 키 생성
    private Key secretKey;

    @PostConstruct
    public void init() {
        // 안전하고 충분히 긴 키 생성
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        System.out.println("Generated Secret Key: " + secretKey);
    }

    public String generateToken(String username, List<String> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey)  // 새로 생성한 비밀 키 사용
                .compact();
    }


    public Authentication getAuthentication(String jwt){
        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));
        return  new UsernamePasswordAuthenticationToken(
                userDetails,"",userDetails.getAuthorities()
        );
    }

    public String getUsername(String token){
        return this.parseClaims(token).getSubject();
    }

    public Boolean validateToken(String token){
        if (!StringUtils.hasText(token))
            return false;
        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token){
        try{
            return  Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }

    }
}
