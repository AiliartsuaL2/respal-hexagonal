package hckt.respalhex.auth.adapter.out.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.google.gson.Gson;
import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.global.config.CleanUp;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import hckt.respalhex.member.application.port.in.SignInUseCase;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessagingException;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

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
        void test1() {
            //given
            PostMemberRequestDto requestDto = new PostMemberRequestDto(EMAIL, PASSWORD, NICKNAME, PICTURE, PROVIDER);
            postMemberUseCase.create(requestDto);

            LogInRequestDto logInRequestDto = new LogInRequestDto(EMAIL, PASSWORD);

            //when
            Long memberId = loginMemberMessagePublisher.signIn(logInRequestDto);

            //then
            assertThat(memberId).isEqualTo(1L);
        }

        @Test
        @DisplayName("통신 간 Timeout Exception 발생 시 MessagingException이 발생한다.")
        void test2() throws TimeoutException {
            //given
            String requestUrl = "requestUrl";
            AmazonSQSRequester requesterMock = mock(AmazonSQSRequester.class);
            LogInRequestDto logInRequestDto = new LogInRequestDto(EMAIL, PASSWORD);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(requestUrl)
                    .messageBody(new Gson().toJson(logInRequestDto))
                    .build();

            when(requesterMock.sendMessageAndGetResponse(request, 2, TimeUnit.SECONDS)).thenThrow(TimeoutException.class);
            LoginMemberMessagePublisher loginMemberMessagePublisherMock = new LoginMemberMessagePublisher(requestUrl, requesterMock);

            //when & then
            assertThatThrownBy(() -> loginMemberMessagePublisherMock.signIn(logInRequestDto))
                    .isInstanceOf(MessagingException.class)
                    .hasMessage(ErrorMessage.COMMUNICATION_EXCEPTION.getMessage());
        }

        // todo Exception 발생시 DLQ 작업 할 것
        @Test
        @DisplayName("응답받은 회원 ID가 회원 ID 타입 (long)이 아닌 경우 MessagingException이 발생한다.")
        void test3() throws TimeoutException {
            //given
            String requestUrl = "requestUrl";
            LogInRequestDto logInRequestDto = new LogInRequestDto(EMAIL, PASSWORD);
            String body = new Gson().toJson(logInRequestDto);
            AmazonSQSRequester requesterMock = mock(AmazonSQSRequester.class);
            Message message = Message.builder()
                    .body(body)
                    .build();

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(requestUrl)
                    .messageBody(body)
                    .build();

            when(requesterMock.sendMessageAndGetResponse(request, 2, TimeUnit.SECONDS)).thenReturn(message);
            LoginMemberMessagePublisher loginMemberMessagePublisherMock = new LoginMemberMessagePublisher(requestUrl, requesterMock);

            //when & then
            assertThatThrownBy(() -> loginMemberMessagePublisherMock.signIn(logInRequestDto))
                    .isInstanceOf(MessagingException.class)
                    .hasMessage(ErrorMessage.COMMUNICATION_EXCEPTION.getMessage());
        }
    }
}
