package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.global.annotation.PersistenceAdapter;
import hckt.respalhex.member.application.port.out.CommandOAuthPort;
import hckt.respalhex.member.domain.OAuth;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class OAuthPersistenceAdapter implements CommandOAuthPort {
    private final OAuthRepository oAuthRepository;
    @Override
    public void create(OAuth oAuth) {
        oAuthRepository.save(oAuth);
    }
}
