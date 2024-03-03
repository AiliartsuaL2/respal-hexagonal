package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.Member;
import hckt.respalhex.member.domain.converter.Provider;
import java.util.Optional;

interface MemberRepositoryCustom {
    Optional<Member> findMemberByEmailAndProvider(String email, Provider provider);
}
