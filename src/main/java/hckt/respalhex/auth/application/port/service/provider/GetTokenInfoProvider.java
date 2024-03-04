package hckt.respalhex.auth.application.port.service.provider;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface GetTokenInfoProvider {

    String getPayload(String token);

    boolean isValid(String token);

    void validateAndThrow(String token);

    Authentication getAuthentication(String accessToken);

    String resolveToken(HttpServletRequest request);
}
