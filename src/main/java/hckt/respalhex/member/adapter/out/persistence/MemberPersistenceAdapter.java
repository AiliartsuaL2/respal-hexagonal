package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.global.annotation.PersistenceAdapter;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import hckt.respalhex.member.domain.Member;

import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
class MemberPersistenceAdapter implements LoadMemberPort, CommandMemberPort {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public void create(Member member) {
        MemberEntity memberEntity = memberMapper.mapDomainToEntity(member);
        memberRepository.save(memberEntity);
    }

    @Override
    public Optional<Member> loadMember(Long id) {
        return memberRepository.findById(id)
                .map(memberMapper::mapEntityToDomain);
    }
}
