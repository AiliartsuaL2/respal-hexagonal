package hckt.respalhex.member.adapter.in.message;

import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.AmazonSQSResponderClientBuilder;
import com.amazonaws.services.sqs.MessageContent;
import hckt.respalhex.global.annotation.MessageQueue;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import io.awspring.cloud.sqs.annotation.SqsListener;

@MessageQueue
public class LoginMemberMessageListener {
    private final AmazonSQSResponder sqsResponder;
    private final SignInUseCase signInUseCase;

    public LoginMemberMessageListener(SqsClient sqsClient, SignInUseCase signInUseCase) {
        this.sqsResponder = AmazonSQSResponderClientBuilder.standard().withAmazonSQS(sqsClient).build();
        this.signInUseCase = signInUseCase;
    }

    @SqsListener("${spring.cloud.aws.sqs.queue-name}")
    public void messageListener(String message) {
        System.out.println("Listener: " + message);
    }

    public void handleLoginRequest(Message message) {
        // Process the login and return a serialized result.
        String body = message.body();
        Long memberId = signInUseCase.signIn(body, body);

        // Extract the URL of the temporary queue from the message attribute
        // and send the response to the temporary queue.
        sqsResponder.sendResponseMessage(MessageContent.fromMessage(message),new MessageContent(String.valueOf(memberId)));
    }
}
