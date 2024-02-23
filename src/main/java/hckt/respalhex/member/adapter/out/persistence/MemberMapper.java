package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.global.annotation.EntityMapper;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.domain.Member;

@EntityMapper
class MemberMapper {
    MemberEntity mapDomainToEntity(Member member) {
        return MemberEntity.create(member);
    }

    Member mapEntityToDomain(MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getNickname(), memberEntity.getPicture());
    }
}
