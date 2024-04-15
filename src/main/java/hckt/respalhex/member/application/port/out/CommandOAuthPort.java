package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.domain.OAuth;

public interface CommandOAuthPort {

    void create(OAuth oAuth);
}
