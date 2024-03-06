package hckt.respalhex.auth.application.port.in;

import hckt.respalhex.auth.application.port.service.provider.GetTokenInfoProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

//해당 클래스는 JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달.
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final GetTokenInfoProvider getTokenInfoProvider;
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, JwtException {
        // 헤더에서 JWT 를 받아옴
        String token = getTokenInfoProvider.resolveToken((HttpServletRequest) request);

        //Bearer 토큰인지 확인
        if (token != null && token.startsWith(TOKEN_PREFIX)){
            token = token.replace(TOKEN_PREFIX,"");
            // token validation
            getTokenInfoProvider.validateAndThrow(token);
            Authentication authentication = getTokenInfoProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // else 타는경우 doFIlter 내부에서 exception 후 CustomAuthenticationEntryPoint로 감
        // 로그인 정상 > if문 안타고 chain.doFilter 타서 createToken?
        // 토큰 정상인경우 >> 다음 필터로 넘어감
        // 토큰 잘못된경우 try- catch문 타고서,, error
        chain.doFilter(request, response); // 걸려있는 필터를 호출 시키는 메서드
    }
}