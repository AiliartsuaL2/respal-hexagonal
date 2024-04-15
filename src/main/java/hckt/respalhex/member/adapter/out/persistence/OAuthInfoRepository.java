package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.OAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface OAuthInfoRepository extends JpaRepository<OAuthInfo, Long> {
    Optional<OAuthInfo> findOAuthInfoByUid(String uid);
}
