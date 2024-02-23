package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberPersistenceAdapterTest {
    @Autowired
    MemberPersistenceAdapter memberPersistenceAdapter;
    @Autowired
    MemberRepository memberRepository;

    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String PICTURE = "picture";

    @Test
    @DisplayName("회원 생성 후 이메일로 조회시 값이 존재한다.")
    void 회원_생성_후_이메일로_조회시_값이_존재한다() {
        // given
        Member member = new Member(null, EMAIL, PASSWORD, NICKNAME, PICTURE);

        // when
        memberPersistenceAdapter.create(member);
        Optional<MemberEntity> memberEntity = memberRepository.findMemberEntityByEmail(EMAIL);

        // then
        assertThat(memberEntity).isPresent();
    }

    @Test
    @DisplayName("회원 미생성 후 이메일로 조회시 값이 존재하지 않는다.")
    void 회원_미생성_후_이메일로_조회시_값이_존재하지_않는다() {
        // given & when
        Optional<MemberEntity> memberEntity = memberRepository.findMemberEntityByEmail(EMAIL);

        // then
        assertThat(memberEntity).isEmpty();
    }
}