package hckt.respalhex.member.application.port.in;

import hckt.respalhex.member.application.port.dto.request.CreateMemberRequestDto;

public interface PostMemberUseCase {
    void create(CreateMemberRequestDto createMemberDto);
}
