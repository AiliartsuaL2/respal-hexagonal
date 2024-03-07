package hckt.respalhex.auth.adapter.out.persistence;

import hckt.respalhex.auth.application.port.out.LoadUserAccountPort;
import hckt.respalhex.auth.domain.UserAccount;
import hckt.respalhex.global.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@PersistenceAdapter
class UserAccountPersistenceAdapter implements LoadUserAccountPort {
    private final UserAccountRepository userAccountRepository;

    @Override
    public Optional<UserAccount> findUserAccountByMemberId(Long memberId) {
        return userAccountRepository.findUserAccountByMemberId(memberId);
    }
}
