package hckt.respalhex.global.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserAccountEvent {
    private Long memberId;
    private String role;
}
