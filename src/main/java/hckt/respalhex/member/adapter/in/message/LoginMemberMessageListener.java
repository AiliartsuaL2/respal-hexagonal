package hckt.respalhex.member.adapter.in.message;

import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.MessageContent;
import com.google.gson.Gson;
import hckt.respalhex.global.annotation.MessageQueue;
import hckt.respalhex.member.adapter.dto.request.LoginMemberRequestDto;
import hckt.respalhex.member.adapter.dto.request.OAuthLoginMemberRequestDto;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.model.Message;
import io.awspring.cloud.sqs.annotation.SqsListener;

@MessageQueue
@RequiredArgsConstructor
@Slf4j
public class LoginMemberMessageListener {
    private final AmazonSQSResponder sqsResponder;
    private final SignInUseCase signInUseCase;

    @SqsListener("${sqs.signin.request-url.common}")
    public void commonMessageListener(Message message) {
        LoginMemberRequestDto requestDto = new Gson().fromJson(message.body(), LoginMemberRequestDto.class);
        handleLoginRequest(message, () -> signInUseCase.signIn(requestDto.email(), requestDto.password()));
    }

    @SqsListener("${sqs.signin.request-url.oauth}")
    public void oAuthMessageListener(Message message) {
        OAuthLoginMemberRequestDto requestDto = new Gson().fromJson(message.body(), OAuthLoginMemberRequestDto.class);
        handleLoginRequest(message, () -> signInUseCase.signIn(requestDto.client(), requestDto.provider(), requestDto.code()));
    }

    private void handleLoginRequest(Message message, Supplier<Long> signInSupplier) {
        try {
            Long memberId = signInSupplier.get();
            sqsResponder.sendResponseMessage(MessageContent.fromMessage(message), new MessageContent(String.valueOf(memberId)));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            sqsResponder.sendResponseMessage(MessageContent.fromMessage(message), new MessageContent(e.getMessage()));
        }
    }
}
