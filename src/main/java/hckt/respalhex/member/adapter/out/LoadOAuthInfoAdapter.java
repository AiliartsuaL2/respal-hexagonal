package hckt.respalhex.member.adapter.out;

import hckt.respalhex.global.annotation.Adapter;
import hckt.respalhex.member.adapter.out.communicate.OAuthInfoCommunicateAdapter;
import hckt.respalhex.member.adapter.out.persistence.OAuthInfoPersistenceAdapter;
import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.application.port.out.LoadOAuthInfoPort;
import hckt.respalhex.member.domain.OAuthInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
class LoadOAuthInfoAdapter implements LoadOAuthInfoPort {
    private final OAuthInfoCommunicateAdapter oAuthInfoCommunicateAdapter;
    private final OAuthInfoPersistenceAdapter oAuthInfoPersistenceAdapter;

    @Override
    public OAuthInfo loadOAuthInfo(OAuthSignInRequestDto requestDto) {
        return oAuthInfoCommunicateAdapter.loadOAuthInfo(requestDto);
    }

    @Override
    public Optional<OAuthInfo> loadOAuthInfo(String uid) {
        return oAuthInfoPersistenceAdapter.loadOAuthInfo(uid);
    }
}
