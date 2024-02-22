package hckt.respalhex.member.application.service;

import hckt.respalhex.member.application.port.dto.request.CreateMemberRequestDto;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements PostMemberUseCase{
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;

    @Override
    public void create(CreateMemberRequestDto createMemberDto) {
        commandMemberPort.create(createMemberDto);
    }
}
