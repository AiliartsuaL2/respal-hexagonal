package hckt.respalhex.auth.application.port.in;

public interface RenewAccessTokenUseCase {
    String renewAccessToken(String refreshToken);
}
