package hckt.respalhex.auth.application.port.in;

import hckt.respalhex.auth.domain.Token;

public interface CreateTokenUseCase {
    Token create(Long memberId);
}
