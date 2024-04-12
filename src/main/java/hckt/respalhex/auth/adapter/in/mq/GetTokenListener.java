package hckt.respalhex.auth.adapter.in.mq;

import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.MessageContent;
import com.google.gson.Gson;
import hckt.respalhex.auth.application.port.in.CreateTokenUseCase;
import hckt.respalhex.auth.domain.Token;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.global.annotation.MessageQueue;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.model.Message;

@MessageQueue
@Slf4j
@RequiredArgsConstructor
public class GetTokenListener {
    private final AmazonSQSResponder sqsResponder;
    private final CreateTokenUseCase createTokenUseCase;

    @SqsListener("${spring.cloud.aws.sqs.queue-name}")
    public void messageListener(Message message) {
        handleLoginRequest(message);
    }

    // TODO : 예외시 DLQ 로직 추가하기
    private void handleLoginRequest(Message message) {
        try {
            long memberId = Long.parseLong(message.body());
            Token token = createTokenUseCase.create(memberId);
            sqsResponder.sendResponseMessage(MessageContent.fromMessage(message), new MessageContent(new Gson().toJson(token)));
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            sqsResponder.sendResponseMessage(MessageContent.fromMessage(message), new MessageContent(ErrorMessage.INVALID_MEMBER_ID_EXCEPTION.getMessage()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error(e.getMessage());
            sqsResponder.sendResponseMessage(MessageContent.fromMessage(message), new MessageContent(e.getMessage()));
        }
    }
}
