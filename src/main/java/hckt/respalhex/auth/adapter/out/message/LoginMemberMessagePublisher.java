package hckt.respalhex.auth.adapter.out.message;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.amazonaws.services.sqs.AmazonSQSRequesterClientBuilder;
import com.google.gson.Gson;
import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.auth.application.port.out.LoadMemberInfoPort;
import hckt.respalhex.global.annotation.MessageQueue;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@MessageQueue
class LoginMemberMessagePublisher implements LoadMemberInfoPort {
    @Value("${sqs.signin.queue-name}")
    private String queueName;
    private final AmazonSQSRequester sqsRequester;
    private final String requestQueueUrl;

    public LoginMemberMessagePublisher(@Value("${sqs.signin.request}") String requestQueueUrl, SqsClient sqsClient) {
        this.sqsRequester = AmazonSQSRequesterClientBuilder.standard()
                .withAmazonSQS(sqsClient)
                .build();
        this.requestQueueUrl = requestQueueUrl;
    }

    @Override
    // 회원 도메인과 통신하여 memberId를 가져온다.
    public Long signIn(LogInRequestDto requestDto) throws TimeoutException {
        String body = new Gson().toJson(requestDto);
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(requestQueueUrl)
                .messageBody(body)
                .build();

        Message reply = sqsRequester.sendMessageAndGetResponse(request,2, TimeUnit.SECONDS);
        sqsRequester.shutdown();
        return Long.parseLong(reply.body());
    }
}
