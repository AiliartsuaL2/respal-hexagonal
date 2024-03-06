package hckt.respalhex.auth.adapter.in.handler;

import hckt.respalhex.auth.adapter.out.handler.JwtErrorResponseHandler;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import hckt.respalhex.auth.exception.ErrorMessage;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final JwtErrorResponseHandler jwtErrorResponseHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (SignatureException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.SIGNATURE_TOKEN_EXCEPTION);
        } catch (MalformedJwtException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.MALFORMED_TOKEN_EXCEPTION);
        } catch (ExpiredJwtException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.EXPIRED_TOKEN_EXCEPTION);
        } catch (IllegalArgumentException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.INCORRECT_REFRESH_TOKEN_EXCEPTION);
        }
    }
}