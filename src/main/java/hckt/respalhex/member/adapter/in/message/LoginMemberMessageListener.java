package hckt.respalhex.member.adapter.in.message;

import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.AmazonSQSResponderClientBuilder;
import com.amazonaws.services.sqs.MessageContent;
import com.google.gson.Gson;
import hckt.respalhex.global.annotation.MessageQueue;
import hckt.respalhex.member.adapter.dto.request.LoginMemberRequestDto;
import hckt.respalhex.member.application.port.in.SignInUseCase;
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
    public void messageListener(Message message) {
        handleLoginRequest(message);
    }

    public void handleLoginRequest(Message message) {
        String body = message.body();
        Gson gson = new Gson();

        LoginMemberRequestDto requestDto = gson.fromJson(body, LoginMemberRequestDto.class);
        Long memberId = signInUseCase.signIn(requestDto.email(), requestDto.password());

        sqsResponder.sendResponseMessage(MessageContent.fromMessage(message),new MessageContent(String.valueOf(memberId)));
        sqsResponder.shutdown();
    }
}