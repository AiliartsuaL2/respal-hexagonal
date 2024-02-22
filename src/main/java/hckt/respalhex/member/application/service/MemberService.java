package hckt.respalhex.member.application.service;

import hckt.respalhex.member.application.dto.request.CreateMemberRequestDto;
import hckt.respalhex.member.application.port.in.GetMemberUseCase;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import hckt.respalhex.member.domain.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements PostMemberUseCase, GetMemberUseCase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;

    @Override
    public void create(CreateMemberRequestDto createMemberDto) {
        Member member = new Member(createMemberDto.id(), createMemberDto.email());
        commandMemberPort.create(member);
    }

    @Override
    public Optional<Member> getMember(Long id) {
        return loadMemberPort.loadMember(id);
    }
}
