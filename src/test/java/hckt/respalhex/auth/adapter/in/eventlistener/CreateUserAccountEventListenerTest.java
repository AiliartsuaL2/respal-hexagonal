package hckt.respalhex.auth.adapter.in.eventlistener;

import hckt.respalhex.auth.application.port.in.CreateUserAccountUseCase;
import hckt.respalhex.auth.application.port.out.LoadUserAccountPort;
import hckt.respalhex.auth.domain.UserAccount;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.global.event.CreateUserAccountEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.mockito.Mockito.mock;


@SpringBootTest
class CreateUserAccountEventListenerTest {
    CreateUserAccountUseCase createUserAccountUseCase = mock(CreateUserAccountUseCase.class);
    CreateUserAccountEventListener createUserAccountEventListener = new CreateUserAccountEventListener(createUserAccountUseCase);

    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    LoadUserAccountPort loadUserAccountPort;

    @Nested
    @DisplayName("회원 정보 생성 테스트")
    class CreateUserAccountTest {
        @Test
        @DisplayName("이벤트가 발행되면 회원 정보가 생성된다.")
        void test1() {
            // given
            Long memberId = 0L;
            String roleType = "user";

            // when
            publisher.publishEvent(new CreateUserAccountEvent(memberId, roleType));
            Optional<UserAccount> userAccountByMemberId = loadUserAccountPort.findUserAccountByMemberId(memberId);

            // then
            Assertions.assertThat(userAccountByMemberId).isPresent();
        }

        @Test
        @DisplayName("이벤트로 전달되는 객체 자체가 null인 경우 예외 발생")
        void test2() {
            // given & when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_EVENT_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("이벤트로 전달되는 객체의 회원ID 필드가 null인 경우 예외 발생")
        void test3() {
            // given
            Long memberId = null;
            String roleType = "user";

            // when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(new CreateUserAccountEvent(memberId, roleType)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("이벤트로 전달되는 객체의 role이 null인 경우 예외 발생")
        void test4() {
            // given
            Long memberId = 0L;
            String roleType = null;

            // when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(new CreateUserAccountEvent(memberId, roleType)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_ROLE_TYPE_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("이벤트로 전달되는 객체의 role이 빈 값인 경우 예외 발생")
        void test5() {
            // given
            Long memberId = 0L;
            String roleType = "";

            // when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(new CreateUserAccountEvent(memberId, roleType)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_ROLE_TYPE_EXCEPTION.getMessage());
        }
    }
}