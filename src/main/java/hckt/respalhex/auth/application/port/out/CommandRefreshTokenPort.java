package hckt.respalhex.auth.application.port.out;

public interface CommandRefreshTokenPort {
    void create(String refreshToken);
    void delete(String refreshToken);
}
