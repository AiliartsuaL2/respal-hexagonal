package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.application.dto.request.CreateMemberRequestDto;
import hckt.respalhex.member.domain.Member;

public interface CommandMemberPort {
    void create(Member member);
}
