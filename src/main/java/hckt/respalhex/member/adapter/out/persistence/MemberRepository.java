package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.Member;
import hckt.respalhex.member.domain.converter.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findMemberByEmail(String email);
}
