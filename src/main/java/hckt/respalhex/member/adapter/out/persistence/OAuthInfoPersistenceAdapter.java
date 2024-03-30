package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.global.annotation.PersistenceAdapter;
import hckt.respalhex.member.application.port.out.CommandOAuthInfoPort;
import hckt.respalhex.member.domain.OAuthInfo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class OAuthInfoPersistenceAdapter implements CommandOAuthInfoPort {
    private final OAuthInfoRepository oAuthInfoRepository;

    public void save(OAuthInfo oAuthInfo) {
        oAuthInfoRepository.save(oAuthInfo);
    }

    public Optional<OAuthInfo> loadOAuthInfo(String uid) {
        return oAuthInfoRepository.findOAuthInfoByUid(uid);
    }
}
