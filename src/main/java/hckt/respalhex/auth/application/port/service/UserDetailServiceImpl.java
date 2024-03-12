package hckt.respalhex.auth.application.port.service;

import hckt.respalhex.auth.application.port.in.CreateUserAccountUseCase;
import hckt.respalhex.auth.application.port.out.CommandUserAccountPort;
import hckt.respalhex.auth.application.port.out.LoadUserAccountPort;
import hckt.respalhex.auth.domain.UserAccount;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.auth.exception.NotRegisteredUserAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailServiceImpl implements UserDetailsService, CreateUserAccountUseCase {
    private final LoadUserAccountPort loadUserAccountPort;
    private final CommandUserAccountPort commandUserAccountPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserAccountPort.findUserAccountByMemberId(Long.parseLong(username)).orElseThrow(
                () -> new NotRegisteredUserAccountException(ErrorMessage.NOT_EXIST_USER_ACCOUNT_EXCEPTION.getMessage()));
    }

    @Override
    @Transactional
    public void create(UserAccount userAccount) {
        commandUserAccountPort.create(userAccount);
    }
}
