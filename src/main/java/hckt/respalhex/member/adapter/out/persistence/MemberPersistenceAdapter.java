package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.global.annotation.PersistenceAdapter;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import hckt.respalhex.member.domain.Member;

import hckt.respalhex.member.domain.converter.Provider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
class MemberPersistenceAdapter implements LoadMemberPort, CommandMemberPort {
    private final MemberRepository memberRepository;

    @Override
    public void create(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Optional<Member> loadMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Override
    public Optional<Member> loadMemberByEmailAndProvider(String email, Provider provider) {
        return memberRepository.findMemberByEmailAndProvider(email, provider);
    }
}
