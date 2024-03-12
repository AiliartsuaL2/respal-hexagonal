package hckt.respalhex.auth.adapter.out.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import hckt.respalhex.auth.exception.ErrorMessage;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final JwtErrorResponseHandler jwtErrorResponseHandler;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.NOT_EXIST_TOKEN_INFO_EXCEPTION);
    }
}

