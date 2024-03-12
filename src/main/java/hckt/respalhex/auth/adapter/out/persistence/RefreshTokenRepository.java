package hckt.respalhex.auth.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByToken(String token);
    @Query("SELECT rt.token FROM RefreshToken rt WHERE rt.keyId = :keyId")
    Optional<String> findTokenByKeyId(Long keyId);
}
