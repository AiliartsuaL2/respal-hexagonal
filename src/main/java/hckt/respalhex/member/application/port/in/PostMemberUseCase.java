package hckt.respalhex.member.application.port.in;

import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;

public interface PostMemberUseCase {
    void create(PostMemberRequestDto createMemberDto);
}
