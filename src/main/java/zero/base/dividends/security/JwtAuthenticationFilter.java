package zero.base.dividends.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //1.요청이 들어 올때마다

    public static final String TOKEN_HEADER = "Authorization"; //어떤 키를 가지고있는지
    public static final String TOKEN_PREFIX = "Bearer "; //인증 타입

    private final TokenProvider tokenProvider;

    //2.필터가 컨트롤러보다 먼저 실행 되면서
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String token = this.resolveTokenFromRequest(request);

        //4.토큰이 있고 그 토큰이 유효한지 확인
        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)){
            Authentication auth = this.tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request,response);
    }

    //3.토큰있는지 헤더 확인
    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        // 토큰이 존재하고, 토큰이 "Bearer "로 시작하는지 확인
        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        // 조건을 만족하지 않으면 null 반환
        return null;
    }

}
