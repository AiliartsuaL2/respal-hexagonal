package hckt.respalhex.member.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
