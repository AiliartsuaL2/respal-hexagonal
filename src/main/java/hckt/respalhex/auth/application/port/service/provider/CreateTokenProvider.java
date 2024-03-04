package hckt.respalhex.auth.application.port.service.provider;

public interface CreateTokenProvider {
    String createAccessToken(String payload);

    String createRefreshToken(String payload);
}
