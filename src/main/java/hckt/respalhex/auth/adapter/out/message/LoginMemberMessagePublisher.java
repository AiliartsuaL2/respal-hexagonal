package hckt.respalhex.auth.adapter.out.message;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.google.gson.Gson;
import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.auth.application.port.out.LoadMemberInfoPort;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.global.annotation.MessageQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@MessageQueue
class LoginMemberMessagePublisher implements LoadMemberInfoPort {
    private final AmazonSQSRequester sqsRequester;
    private final String requestQueueUrl;

    public LoginMemberMessagePublisher(@Value("${sqs.signin.request}") String requestQueueUrl, AmazonSQSRequester sqsRequester) {
        this.sqsRequester = sqsRequester;
        this.requestQueueUrl = requestQueueUrl;
    }

    @Override
    // 회원 도메인과 통신하여 memberId를 가져온다.
    public Long signIn(LogInRequestDto requestDto) {
        String body = new Gson().toJson(requestDto);
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(requestQueueUrl)
                .messageBody(body)
                .build();
         try {
            Message reply = sqsRequester.sendMessageAndGetResponse(request,2, TimeUnit.SECONDS);
            return Long.parseLong(reply.body());
        } catch (TimeoutException | NumberFormatException e) {
            throw new MessagingException(ErrorMessage.COMMUNICATION_EXCEPTION.getMessage());
        } finally {
            sqsRequester.shutdown();
        }
    }
}
