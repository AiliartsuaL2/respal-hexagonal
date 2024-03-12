package hckt.respalhex.auth.application.port.out;

import hckt.respalhex.auth.domain.UserAccount;

public interface CommandUserAccountPort {
    void create(UserAccount userAccount);
}
