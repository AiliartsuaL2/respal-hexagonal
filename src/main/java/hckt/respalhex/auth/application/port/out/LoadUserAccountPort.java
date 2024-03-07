package hckt.respalhex.auth.application.port.out;

import hckt.respalhex.auth.domain.UserAccount;

import java.util.Optional;

public interface LoadUserAccountPort {
    Optional<UserAccount> findUserAccountByMemberId(Long memberId);
}
