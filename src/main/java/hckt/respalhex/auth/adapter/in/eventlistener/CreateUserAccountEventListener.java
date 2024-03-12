package hckt.respalhex.auth.adapter.in.eventlistener;

import hckt.respalhex.auth.application.port.in.CreateUserAccountUseCase;
import hckt.respalhex.auth.domain.UserAccount;
import hckt.respalhex.global.annotation.Event;
import hckt.respalhex.global.event.CreateUserAccountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
@Event
@RequiredArgsConstructor
public class CreateUserAccountEventListener {
    private final CreateUserAccountUseCase createUserAccountUseCase;

    @EventListener(CreateUserAccountEvent.class)
    public void createUserAccount(CreateUserAccountEvent event) {
        createUserAccountUseCase.create(UserAccount.create(event.getMemberId(), event.getRole()));
    }
}
