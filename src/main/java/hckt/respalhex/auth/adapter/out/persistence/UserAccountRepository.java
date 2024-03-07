package hckt.respalhex.auth.adapter.out.persistence;

import hckt.respalhex.auth.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findUserAccountByMemberId(Long memberId);
}
