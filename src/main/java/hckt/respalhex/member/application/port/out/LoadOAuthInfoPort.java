package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.domain.OAuthInfo;
import java.util.Optional;

public interface LoadOAuthInfoPort {
    OAuthInfo loadOAuthInfo(OAuthSignInRequestDto requestDto);
    Optional<OAuthInfo> loadOAuthInfo(String uid);
}
