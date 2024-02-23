package hckt.respalhex.member.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findMemberEntityByEmail(String email);
}
