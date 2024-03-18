package hckt.respalhex.member.adapter.in.message;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.MessageContent;
import com.google.gson.Gson;
import hckt.respalhex.member.adapter.dto.request.LoginMemberRequestDto;
import org.assertj.core.api.Assertions;
import software.amazon.awssdk.services.sqs.model.Message;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LoginMemberMessageListenerTest {
    AmazonSQSResponder responder = mock(AmazonSQSResponder.class);
    SignInUseCase signInUseCase = mock(SignInUseCase.class);
    LoginMemberMessageListener loginMemberMessageListener = new LoginMemberMessageListener(responder, signInUseCase);

    @Nested
    @DisplayName("로그인 이벤트 핸들링 테스트")
    class HandleLoginRequest {
        private static Long MEMBER_ID = 0L;
        private static String EMAIL = "ailiartsual2@gmail.com";
        private static String PASSWORD = "password";
        @Test
        @DisplayName("로그인 이벤트 발생시 회원 ID를 응답한다.")
        void test1() {
            //given
            LoginMemberRequestDto requestDto = new LoginMemberRequestDto(EMAIL, PASSWORD);
            Message message = Message.builder()
                    .body(new Gson().toJson(requestDto))
                    .build();
            when(signInUseCase.signIn(EMAIL, PASSWORD)).thenReturn(MEMBER_ID);

            //when
            loginMemberMessageListener.handleLoginRequest(message);

            //then
            assertThatNoException();
        }
    }
}
