package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.application.port.dto.request.CreateMemberRequestDto;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import hckt.respalhex.member.domain.Member;
import hckt.respalhex.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberPersistenceAdapter implements LoadMemberPort, CommandMemberPort {
    private final MemberRepository memberRepository;

    @Override
    public void create(CreateMemberRequestDto createMemberRequestDto) {
        Member member = Member.create(createMemberRequestDto);
        memberRepository.save(member);
    }

    @Override
    public Member loadMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 회원이 존재하지 않습니다"));
    }
}
