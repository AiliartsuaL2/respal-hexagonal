package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.application.dto.response.GetTokenResponseDto;

public interface LoadTokenPort {
    GetTokenResponseDto loadToken(Long memberId);
}
