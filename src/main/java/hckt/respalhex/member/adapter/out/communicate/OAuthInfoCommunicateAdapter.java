package hckt.respalhex.member.adapter.out.communicate;

import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.domain.OAuthInfo;

public interface OAuthInfoCommunicateAdapter {
    OAuthInfo loadOAuthInfo(OAuthSignInRequestDto requestDto);
}
