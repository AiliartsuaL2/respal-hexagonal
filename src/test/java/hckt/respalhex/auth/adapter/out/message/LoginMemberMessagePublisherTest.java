package hckt.respalhex.auth.adapter.out.message;

import static org.assertj.core.api.Assertions.assertThat;

import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.global.config.CleanUp;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class LoginMemberMessagePublisherTest {
    @Autowired
    CleanUp cleanUp;

    @Autowired
    LoginMemberMessagePublisher loginMemberMessagePublisher;
    @Autowired
    PostMemberUseCase postMemberUseCase;
    @Autowired
    SignInUseCase signInUseCase;

    @AfterEach
    void afterEach() {
        // 테스트 이후 DB 초기화
        cleanUp.all();
    }

    @Nested
    @DisplayName("로그인 이벤트 발송 테스트")
    class SignIn {
        private static final String EMAIL = "ailiartsual2@gmail.com";
        private static final String PASSWORD = "123456789a!";
        private static final String NICKNAME = "nickname";
        private static final String PICTURE = "picture";
        private static final String PROVIDER = "common";
        @Test
        @DisplayName("로그인시 회원 ID를 응답 받는다.")
        void test1() throws TimeoutException {
            //given
            PostMemberRequestDto requestDto = new PostMemberRequestDto(EMAIL, PASSWORD, NICKNAME, PICTURE, PROVIDER);
            postMemberUseCase.create(requestDto);

            LogInRequestDto logInRequestDto = new LogInRequestDto(EMAIL, PASSWORD);

            //when
            Long memberId = loginMemberMessagePublisher.signIn(logInRequestDto);

            //then
            assertThat(memberId).isEqualTo(1L);
        }
    }
}
