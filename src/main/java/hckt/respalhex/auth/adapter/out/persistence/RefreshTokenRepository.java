package hckt.respalhex.auth.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByRefreshToken(String refreshToken);

    Optional<String> findRefreshTokenByKeyId(Long keyId);
}
