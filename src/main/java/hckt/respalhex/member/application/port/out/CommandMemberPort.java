package hckt.respalhex.member.application.port.out;

import hckt.respalhex.member.application.port.dto.request.CreateMemberRequestDto;

public interface CommandMemberPort {
    void create(CreateMemberRequestDto createMemberRequestDto);
}
