package hckt.respalhex.member.adapter.in.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.util.Constants;
import com.google.gson.Gson;
import hckt.respalhex.global.config.AwsSqsConfig;
import hckt.respalhex.member.adapter.dto.request.LoginMemberRequestDto;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import hckt.respalhex.member.exception.ErrorMessage;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.Message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@SpringBootTest(classes = AwsSqsConfig.class)
class LoginMemberMessageListenerTest {
    private static final String QUEUE_NAME = "respalMessageTestQueue";
    @Autowired
    AmazonSQSResponder responder;
    private static String queueUrl;

    @BeforeAll
    private static void beforeAll(@Autowired AmazonSQSResponder responder) {
        queueUrl = responder.getAmazonSQS().createQueue(CreateQueueRequest.builder().queueName(QUEUE_NAME).build()).queueUrl();
    }

    @AfterAll
    private static void afterAll(@Autowired AmazonSQSResponder responder) {
        responder.getAmazonSQS().deleteQueue(DeleteQueueRequest.builder().queueUrl(queueUrl).build());
    }

    @Nested
    @DisplayName("로그인 이벤트 핸들링 테스트")
    class HandleLoginRequest {
        private static final Long MEMBER_ID = 0L;
        private static final String EMAIL = "ailiartsual2@gmail.com";
        private static final String PASSWORD = "123456789a!";

        SignInUseCase signInUseCaseMock = mock(SignInUseCase.class);
        LoginMemberMessageListener loginMemberMessageListenerMock = new LoginMemberMessageListener(responder, signInUseCaseMock);
        LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(EMAIL, PASSWORD);
        Message message = Message.builder()
                .body(new Gson().toJson(loginMemberRequestDto))
                .messageAttributes(Map.of(Constants.RESPONSE_QUEUE_URL_ATTRIBUTE_NAME,
                        MessageAttributeValue.builder()
                                .stringValue(queueUrl)
                                .build()))
                .build();

        @Test
        @DisplayName("로그인 수신시 messageListener 에서 consume 후 응답한다.")
        void test1() {
            //given
            when(signInUseCaseMock.signIn(loginMemberRequestDto.email(), loginMemberRequestDto.password()))
                    .thenReturn(MEMBER_ID);

            //when
            loginMemberMessageListenerMock.commonMessageListener(message);
            String response = receiveMessage();

            //then
            assertThat(response).isEqualTo(String.valueOf(MEMBER_ID));
        }

        @Test
        @DisplayName("비회원 로그인시 에러 메세지 응답")
        void test2() {
            //given
            when(signInUseCaseMock.signIn(loginMemberRequestDto.email(), loginMemberRequestDto.password()))
                    .thenThrow(new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage()));

            //when
            loginMemberMessageListenerMock.commonMessageListener(message);
            String response = receiveMessage();

            // then
            assertThat(response).isEqualTo(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage());
        }


        @Test
        @DisplayName("비밀번호가 틀린 경우 에러 메세지 응답")
        void test3() {
            //given
            when(signInUseCaseMock.signIn(loginMemberRequestDto.email(), loginMemberRequestDto.password()))
                    .thenThrow(new IllegalArgumentException(ErrorMessage.NOT_MATCH_PASSWORD_EXCEPTION.getMessage()));

            //when
            loginMemberMessageListenerMock.commonMessageListener(message);
            String response = receiveMessage();

            // then
            assertThat(response).isEqualTo(ErrorMessage.NOT_MATCH_PASSWORD_EXCEPTION.getMessage());
        }

        private String receiveMessage() {
            ReceiveMessageResponse receiveMessageResponse = responder.getAmazonSQS()
                    .receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(1).build());
            return receiveMessageResponse.messages().get(0).body();
        }
    }
}
