package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.domain.OAuthInfo;

public interface CommandOAuthInfoPort {
    void save(OAuthInfo oAuthInfo);
}
