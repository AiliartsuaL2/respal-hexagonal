package hckt.respalhex.member.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MemberTest {
    @Nested
    @DisplayName("생성자 테스트")
    class Constructor {
        @Test
        @DisplayName("회원 생성시 비밀번호가 암호화되어 저장된다")
        void 회원_생성시_비밀번호가_암호화되어_저장된다() {
            // given
            String password = "비밀번호 1234";

            // when
            Member member = Member.builder()
                    .password(password)
                    .build();

            // then
            Assertions.assertThat(member.password().length()).isEqualTo(60);
        }

        @Test
        @DisplayName("이미지 미설정후 생성시 이미지가 gravatar 이미지로 생성된다.")
        void 이미지_미설정후_생성시_이미지가_gravatar_이미지로_생성된다() {
            // given
            String password = "비밀번호 1234";

            // when
            Member member = Member.builder()
                    .password(password)
                    .build();

            // then
            Assertions.assertThat(member.picture()).isNotNull();
        }
    }
}