package hckt.respalhex.member.adapter.out.persistence;

import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
        PostMemberRequestDto requestDto = PostMemberRequestDto.builder()
                .nickname(NICKNAME)
                .picture(PICTURE)
                .password(PASSWORD)
                .email(EMAIL)
                .build();
        Member member = Member.create(requestDto);
        memberPersistenceAdapter.create(member);

        // when
        Optional<Member> memberEntity = memberRepository.findMemberEntityByEmail(EMAIL);

        // then
        assertThat(memberEntity).isPresent();
    }

    @Test
    @DisplayName("회원 미생성 후 이메일로 조회시 값이 존재하지 않는다.")
    void 회원_미생성_후_이메일로_조회시_값이_존재하지_않는다() {
        // given & when
        Optional<Member> memberEntity = memberRepository.findMemberEntityByEmail(EMAIL);

        // then
        assertThat(memberEntity).isEmpty();
    }
}
