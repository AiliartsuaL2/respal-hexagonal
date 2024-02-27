package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberEntityByEmail(String email);
}
