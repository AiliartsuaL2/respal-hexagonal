package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("엔티티 도메인 매퍼 테스트")
class MemberMapperTest {
    private static final MemberMapper MEMBER_MAPPER = new MemberMapper();
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String NICKNAME = "nickname";
    private static final String PICTURE = "picture";

    @Test
    @DisplayName("도메인에서 엔티티로 변경시 모든 값이 동일하다")
    void 도메인_에서_엔티티로_변경_테스트() {
        // given
        Member member = new Member(null, EMAIL, PASSWORD, NICKNAME, PICTURE);

        // when
        MemberEntity memberEntity = MEMBER_MAPPER.mapDomainToEntity(member);

        // then
        assertThat(memberEntity.getId()).isEqualTo(member.id());
        assertThat(memberEntity.getEmail()).isEqualTo(member.email());
        assertThat(memberEntity.getPassword()).isEqualTo(member.password());
        assertThat(memberEntity.getNickname()).isEqualTo(member.nickname());
        assertThat(memberEntity.getPicture()).isEqualTo(member.picture());
    }

    @Test
    @DisplayName("엔티티에서 도메인으로 변경시 모든 값이 동일하다")
    void 엔티티_에서_도메인으로_변경_테스트() {
        // given
        Member member = new Member(null, EMAIL, PASSWORD, NICKNAME, PICTURE);
        MemberEntity memberEntity = MemberEntity.create(member);

        // when
        Member convertedMember = MEMBER_MAPPER.mapEntityToDomain(memberEntity);

        // then
        assertThat(convertedMember.id()).isEqualTo(memberEntity.getId());
        assertThat(convertedMember.email()).isEqualTo(memberEntity.getEmail());
        assertThat(convertedMember.password()).isEqualTo(memberEntity.getPassword());
        assertThat(convertedMember.nickname()).isEqualTo(memberEntity.getNickname());
        assertThat(convertedMember.picture()).isEqualTo(memberEntity.getPicture());
    }
}