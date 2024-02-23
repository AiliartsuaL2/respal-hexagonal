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
        return Member.of(PostMemberRequestDto.builder()
                .email(memberEntity.getEmail())
                .password(memberEntity.getPassword())
                .picture(memberEntity.getPicture())
                .nickname(memberEntity.getNickname())
                .build());
    }
}
