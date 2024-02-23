package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.global.annotation.EntityMapper;
import hckt.respalhex.member.domain.Member;

@EntityMapper
class MemberMapper {
    MemberEntity mapDomainToEntity(Member member) {
        return MemberEntity.create(member);
    }

    Member mapEntityToDomain(MemberEntity memberEntity) {
        return Member.builder()
                .id(memberEntity.getId())
                .password(memberEntity.getPassword())
                .nickname(memberEntity.getNickname())
                .picture(memberEntity.getPicture())
                .build();
    }
}
