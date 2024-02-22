package hckt.respalhex.member.application.port.in;

import hckt.respalhex.member.application.dto.request.CreateMemberRequestDto;
import hckt.respalhex.member.domain.Member;
import java.util.Optional;

public interface PostMemberUseCase {
    void create(CreateMemberRequestDto createMemberDto);
}
