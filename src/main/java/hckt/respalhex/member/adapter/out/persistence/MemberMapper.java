package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.global.annotation.EntityMapper;
import hckt.respalhex.member.domain.Member;

@EntityMapper
class MemberMapper {
    MemberEntity mapDomainToEntity(Member member) {
        return MemberEntity.create(member.email());
    }

    Member mapEntityToDomain(MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getEmail());
    }
}
