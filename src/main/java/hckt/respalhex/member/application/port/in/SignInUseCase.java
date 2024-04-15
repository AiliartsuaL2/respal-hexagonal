package hckt.respalhex.member.application.port.in;

import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.application.dto.request.SignInMemberRequestDto;
import hckt.respalhex.member.exception.OAuthSignInException;

public interface SignInUseCase {
    Long signIn(SignInMemberRequestDto requestDto);
    Long signIn(OAuthSignInRequestDto requestDto) throws OAuthSignInException;
}
