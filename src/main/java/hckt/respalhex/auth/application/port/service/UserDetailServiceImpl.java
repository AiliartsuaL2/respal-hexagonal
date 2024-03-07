package hckt.respalhex.auth.application.port.service;

import hckt.respalhex.auth.application.port.out.LoadUserAccountPort;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.auth.exception.NotRegisteredUserAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final LoadUserAccountPort userAccountPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountPort.findUserAccountByMemberId(Long.parseLong(username)).orElseThrow(
                () -> new NotRegisteredUserAccountException(ErrorMessage.NOT_EXIST_USER_ACCOUNT_EXCEPTION.getMessage()));
    }
}
