package hckt.respalhex.auth.adapter.out.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.MessageContent;
import com.amazonaws.services.sqs.util.Constants;
import com.google.gson.Gson;
import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.global.config.AwsSqsConfig;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessagingException;
import software.amazon.awssdk.services.sqs.model.*;

@SpringBootTest(classes = AwsSqsConfig.class)
class LoginMemberMessagePublisherTest {
    private static final String QUEUE_NAME = "respalMessageTestQueue";
    @Autowired
    AmazonSQSRequester requester;
    @Autowired
    AmazonSQSResponder responder;
    private static String queueUrl;

    @BeforeAll
    private static void beforeAll(@Autowired AmazonSQSRequester requester) {
        queueUrl = requester.getAmazonSQS()
                .createQueue(CreateQueueRequest
                        .builder()
                        .queueName(QUEUE_NAME)
                        .build())
                .queueUrl();
    }

    @Nested
    @DisplayName("로그인 이벤트 발송 테스트")
    class SignIn {
        private static final String EMAIL = "ailiartsual2@gmail.com";
        private static final String PASSWORD = "123456789a!";

        LoginMemberMessagePublisher loginMemberMessagePublisher = new LoginMemberMessagePublisher(queueUrl, requester);
        LogInRequestDto logInRequestDto = new LogInRequestDto(EMAIL, PASSWORD);
        Message message = Message.builder()
                .body(new Gson().toJson(logInRequestDto))
                .messageAttributes(Map.of(Constants.RESPONSE_QUEUE_URL_ATTRIBUTE_NAME,
                        MessageAttributeValue.builder()
                                .stringValue(queueUrl)
                                .build()))
                .build();

        @Test
        @DisplayName("로그인시 회원 ID를 받아온다.")
        void test1() {
            //given
            Long memberId = 0L;
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            ExecutorService executorService2 = Executors.newSingleThreadExecutor();

            executorService.submit(() -> {
                // 임의 전송
                responder.sendResponseMessage(MessageContent.fromMessage(message),new MessageContent(String.valueOf(memberId)));
            });

            executorService2.submit(() -> {
                // when
                Long expectedMemberId = loginMemberMessagePublisher.signIn(logInRequestDto);

                //then
                assertThat(expectedMemberId).isEqualTo(memberId);
            });

            executorService.shutdown();
            executorService2.shutdown();
        }

        @Test
        @DisplayName("통신 간 Timeout Exception 발생 시 MessagingException이 발생한다.")
        void test2() throws TimeoutException {
            //given & when & then
            assertThatThrownBy(() -> loginMemberMessagePublisher.signIn(logInRequestDto))
                    .isInstanceOf(MessagingException.class)
                    .hasMessage(ErrorMessage.COMMUNICATION_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("응답받은 회원 ID가 회원 ID 타입 (long)이 아닌 경우 MessagingException이 발생한다.")
        void test3() throws TimeoutException {
            //given
            String memberId = "invalid";
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            ExecutorService executorService2 = Executors.newSingleThreadExecutor();

            executorService.submit(() -> {
                responder.sendResponseMessage(MessageContent.fromMessage(message),new MessageContent(memberId));
            });

            //when & then
            executorService2.submit(() -> {
                assertThatThrownBy(() -> loginMemberMessagePublisher.signIn(logInRequestDto))
                        .isInstanceOf(MessagingException.class)
                        .hasMessage(ErrorMessage.COMMUNICATION_EXCEPTION.getMessage());
            });

            executorService.shutdown();
            executorService2.shutdown();
        }
    }
}
