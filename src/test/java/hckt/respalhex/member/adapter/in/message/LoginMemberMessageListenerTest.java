package hckt.respalhex.member.adapter.in.message;

import static org.assertj.core.api.Assertions.assertThat;
import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.google.gson.Gson;
import hckt.respalhex.global.config.CleanUp;
import hckt.respalhex.member.adapter.dto.request.LoginMemberRequestDto;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.sqs.model.Message;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@SpringBootTest
@ActiveProfiles("test")
class LoginMemberMessageListenerTest {

    @Autowired
    CleanUp cleanUp;
    @Autowired
    AmazonSQSRequester requester;
    @Autowired
    LoginMemberMessageListener loginMemberMessageListener;
    @Autowired
    PostMemberUseCase postMemberUseCase;

    @Value("${sqs.signin.request}")
    String requestQueueUrl;

    @AfterEach
    void afterEach() {
        // 테스트 이후 DB 초기화
        cleanUp.all();
    }


    @Nested
    @DisplayName("로그인 이벤트 핸들링 테스트")
    class HandleLoginRequest {

        private static final String EMAIL = "ailiartsual2@gmail.com";
        private static final String PASSWORD = "123456789a!";
        private static final String NICKNAME = "nickname";
        private static final String PICTURE = "picture";
        private static final String PROVIDER = "common";

        @Test
        @DisplayName("로그인 이벤트 발생시 messageListener 에서 consume 후 응답한다.")
        void test1() throws TimeoutException {
            //given
            PostMemberRequestDto postMemberRequestDto = new PostMemberRequestDto(EMAIL, PASSWORD, NICKNAME, PICTURE, PROVIDER);
            postMemberUseCase.create(postMemberRequestDto);

            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(EMAIL, PASSWORD);
            String body = new Gson().toJson(loginMemberRequestDto);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(requestQueueUrl)
                    .messageBody(body)
                    .build();

            //when
            Message reply = requester.sendMessageAndGetResponse(request,2, TimeUnit.SECONDS);

            //then
            assertThat(reply.body()).isEqualTo("1");
        }
    }
}
