package hckt.respalhex.auth.adapter.out.eventpublisher;

import hckt.respalhex.auth.application.port.in.CreateTokenUseCase;
import hckt.respalhex.auth.domain.Token;
import hckt.respalhex.global.annotation.Event;
import hckt.respalhex.global.event.CreateTokenEvent;
import hckt.respalhex.global.event.LoginMemberEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@Event
@RequiredArgsConstructor
public class CreateTokenEventPublisher {
    private final CreateTokenUseCase createTokenUseCase;
    private final ApplicationEventPublisher eventPublisher;

    public void loginMember(LoginMemberEvent event) {
        Token token = createTokenUseCase.create(event.getMemberId());
        CreateTokenEvent tokenEvent = new CreateTokenEvent(token.accessToken(), token.refreshToken());
        eventPublisher.publishEvent(tokenEvent);
    }
}
