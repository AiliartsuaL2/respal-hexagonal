package hckt.respalhex.member.adapter.in.rest;

import hckt.respalhex.member.application.port.in.signinMemberUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginMemberControllerV1 {
    private final signinMemberUseCase signinMemberUseCase;


    @PostMapping("/v1.0.0/signin")
    public void signinMember() {

    }
}
