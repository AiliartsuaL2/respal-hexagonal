package hckt.respalhex.auth.adapter.out.persistence;

import hckt.respalhex.auth.application.port.out.CommandRefreshTokenPort;
import hckt.respalhex.auth.application.port.out.LoadRefreshTokenPort;
import hckt.respalhex.global.annotation.PersistenceAdapter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
class RefreshTokenPersistenceAdapter implements CommandRefreshTokenPort, LoadRefreshTokenPort {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void create(Long keyId, String refreshToken) {
        RefreshToken createdRefreshToken = new RefreshToken(keyId, refreshToken);
        refreshTokenRepository.save(createdRefreshToken);
    }

    @Override
    public void delete(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

    @Override
    public Optional<String> findByKeyId(Long keyId) {
        return refreshTokenRepository.findRefreshTokenByKeyId(keyId);
    }
}
