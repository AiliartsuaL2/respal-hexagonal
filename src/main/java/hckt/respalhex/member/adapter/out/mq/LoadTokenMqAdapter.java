package hckt.respalhex.member.adapter.out.mq;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import hckt.respalhex.member.exception.ErrorMessage;
import hckt.respalhex.global.annotation.MessageQueue;
import hckt.respalhex.member.application.dto.response.GetTokenResponseDto;
import hckt.respalhex.member.application.port.out.LoadTokenPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.messaging.MessagingException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@MessageQueue
@Slf4j
public class LoadTokenMqAdapter implements LoadTokenPort {
    private final AmazonSQSRequester sqsRequester;
    private final String requestQueueUrl;

    public LoadTokenMqAdapter(@Value("${sqs.signin.request}") String requestQueueUrl, AmazonSQSRequester sqsRequester) {
        this.sqsRequester = sqsRequester;
        this.requestQueueUrl = requestQueueUrl;
    }

    @Override
    public GetTokenResponseDto loadToken(Long memberId) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(requestQueueUrl)
                .messageBody(String.valueOf(memberId))
                .build();
        try {
            Message reply = sqsRequester.sendMessageAndGetResponse(request,2, TimeUnit.SECONDS);
            JSONObject json = new JSONObject(reply.body());
            String accessToken = (String) json.get("accessToken");
            String refreshToken = (String) json.get("refreshToken");
            return new GetTokenResponseDto(accessToken, refreshToken);
        } catch (SdkClientException | TimeoutException | JSONException e) {
            log.error(e.getMessage());
            throw new MessagingException(ErrorMessage.AUTH_COMMUNICATION_EXCEPTION.getMessage());
        }
    }
}

