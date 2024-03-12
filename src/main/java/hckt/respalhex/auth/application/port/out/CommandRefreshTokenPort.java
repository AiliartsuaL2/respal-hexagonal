package hckt.respalhex.auth.application.port.out;

public interface CommandRefreshTokenPort {
    void create(Long keyId, String refreshToken);
    void delete(String refreshToken);
}
