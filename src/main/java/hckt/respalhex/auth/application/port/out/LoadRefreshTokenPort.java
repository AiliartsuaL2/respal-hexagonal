package hckt.respalhex.auth.application.port.out;

import java.util.Optional;

public interface LoadRefreshTokenPort {
    Optional<String> findByKeyId(Long keyId);
}
