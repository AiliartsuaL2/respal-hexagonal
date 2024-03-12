package hckt.respalhex.auth.application.port.in;

import hckt.respalhex.auth.domain.UserAccount;

public interface CreateUserAccountUseCase {
    void create(UserAccount userAccount);
}
