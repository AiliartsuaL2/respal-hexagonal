package hckt.respalhex.auth.adapter.out.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import hckt.respalhex.auth.exception.ErrorMessage;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final JwtErrorResponseHandler jwtErrorResponseHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Access Denied Exception: {}", accessDeniedException.getMessage());
        jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.PERMISSION_DENIED_EXCEPTION);
    }
}

