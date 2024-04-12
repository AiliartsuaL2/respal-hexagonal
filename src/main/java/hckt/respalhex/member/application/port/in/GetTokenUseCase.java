package hckt.respalhex.member.application.port.in;

import hckt.respalhex.member.application.dto.response.GetTokenResponseDto;

public interface GetTokenUseCase {
    GetTokenResponseDto getToken(Long memberId);
}
