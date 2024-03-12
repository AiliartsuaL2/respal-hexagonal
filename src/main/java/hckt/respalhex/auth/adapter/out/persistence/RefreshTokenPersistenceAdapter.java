package hckt.respalhex.auth.adapter.out.persistence;

import hckt.respalhex.auth.application.port.out.CommandRefreshTokenPort;
import hckt.respalhex.auth.application.port.out.LoadRefreshTokenPort;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.global.annotation.PersistenceAdapter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@PersistenceAdapter
class RefreshTokenPersistenceAdapter implements CommandRefreshTokenPort, LoadRefreshTokenPort {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void create(Long keyId, String refreshToken) {
        validate(keyId);
        validate(refreshToken);
        RefreshToken createdRefreshToken = new RefreshToken(keyId, refreshToken);
        refreshTokenRepository.save(createdRefreshToken);
    }

    @Override
    public void delete(String refreshToken) {
        validate(refreshToken);
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    @Override
    public Optional<String> findByKeyId(Long keyId) {
        validate(keyId);
        return refreshTokenRepository.findTokenByKeyId(keyId);
    }

    public void validate(Long keyId) {
        if (ObjectUtils.isEmpty(keyId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_KEY_ID_EXCEPTION.getMessage());
        }
    }

    public void validate(String refreshToken) {
        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_REFRESH_TOKEN_EXCEPTION.getMessage());
        }
    }
}
