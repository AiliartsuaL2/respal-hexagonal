package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.global.annotation.PersistenceAdapter;
import hckt.respalhex.member.application.port.out.CommandOAuthInfoPort;
import hckt.respalhex.member.domain.OAuthInfo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class OAuthInfoPersistenceAdapterImpl implements OAuthInfoPersistenceAdapter, CommandOAuthInfoPort  {
    private final OAuthInfoRepository oAuthInfoRepository;

    @Override
    public void save(OAuthInfo oAuthInfo) {
        oAuthInfoRepository.save(oAuthInfo);
    }

    @Override
    public Optional<OAuthInfo> loadOAuthInfo(String uid) {
        return oAuthInfoRepository.findOAuthInfoByUid(uid);
    }
}
