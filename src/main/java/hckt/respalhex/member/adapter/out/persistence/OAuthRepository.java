package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.OAuth;
import org.springframework.data.jpa.repository.JpaRepository;

interface OAuthRepository extends JpaRepository<OAuth, Long> {
}
