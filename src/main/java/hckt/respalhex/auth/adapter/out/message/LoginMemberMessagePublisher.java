package hckt.respalhex.auth.adapter.out.message;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.amazonaws.services.sqs.AmazonSQSRequesterClientBuilder;
import hckt.respalhex.auth.application.port.out.LoadMemberInfoPort;
import hckt.respalhex.global.annotation.MessageQueue;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@MessageQueue
class LoginMemberMessagePublisher implements LoadMemberInfoPort {
    private final AmazonSQSRequester sqsRequester = AmazonSQSRequesterClientBuilder.defaultClient();
    private final String requestQueueUrl;

    public LoginMemberMessagePublisher(@Value("${sqs.signin.request}") String requestQueueUrl) {
        this.requestQueueUrl = requestQueueUrl;
    }

    @Override
    // 회원 도메인과 통신하여 memberId를 가져온다.
    public Long signIn(String email, String password) throws TimeoutException {
        String body = "{email: " + email + ", password: " + password + "}";
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(requestQueueUrl)
                .messageBody(body)
                .build();

        Message reply = sqsRequester.sendMessageAndGetResponse(request,20, TimeUnit.SECONDS);
        return Long.parseLong(reply.body());
    }
}
