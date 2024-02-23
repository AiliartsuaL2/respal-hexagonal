package hckt.respalhex.member.domain;

import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {
    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String PICTURE = "picture";

    @Nested
    @DisplayName("생성자 테스트")
    class Constructor {
        @Test
        @DisplayName("회원 생성시 비밀번호가 암호화되어 저장된다")
        void 회원_생성시_비밀번호가_암호화되어_저장된다() {
            // given & when
            Member member = new Member(null, EMAIL, PASSWORD, NICKNAME, PICTURE);

            // then
            assertThat(member.password().length()).isEqualTo(60);
        }

        @Test
        @DisplayName("이미지 미설정후 생성시 이미지가 gravatar 이미지로 생성된다.")
        void 이미지_미설정후_생성시_이미지가_gravatar_이미지로_생성된다() {
            // given
            String imagePrefix = "gravatar";

            // when
            Member member = new Member(null, EMAIL, PASSWORD, NICKNAME, null);

            // then
            assertThat(member.picture()).contains(imagePrefix);
        }

        @Test
        @DisplayName("이미지 설정후 생성시 이미지가 설정한 이미지로 생성된다.")
        void 이미지_설정후_생성시_이미지가_설정한_이미지로_생성된다() {
            // given
            String image = "설정한 이미지";

            // when
            Member member = new Member(null, EMAIL, PASSWORD, NICKNAME, image);

            // then
            assertThat(member.picture()).isEqualTo(image);
        }
    }

    @Test
    @DisplayName("암호화 비밀번호 일치여부 테스트")
    void 암호화_비밀번호_일치여부_테스트() {
        // given & when
        Member member = new Member(null, EMAIL, PASSWORD, NICKNAME, PICTURE);

        // then
        assertThat(member.matchPassword(PASSWORD)).isTrue();
    }
}