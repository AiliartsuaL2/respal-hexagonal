package hckt.respalhex.member.application.port.in;

import hckt.respalhex.member.application.dto.response.GetMemberResponseDto;

public interface GetMemberUseCase {
    GetMemberResponseDto getMember(Long id);
}
