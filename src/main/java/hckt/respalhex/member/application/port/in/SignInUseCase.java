package hckt.respalhex.member.application.port.in;

import hckt.respalhex.member.application.dto.request.SignInMemberRequestDto;

public interface SignInUseCase {
    Long signIn(SignInMemberRequestDto requestDto);
    Long signIn(String client, String provider, String code);
}
