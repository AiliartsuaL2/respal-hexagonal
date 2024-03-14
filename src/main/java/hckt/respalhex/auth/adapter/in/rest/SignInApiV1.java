package hckt.respalhex.auth.adapter.in.rest;

import hckt.respalhex.auth.adapter.dto.response.SignInResponseDto;
import hckt.respalhex.auth.adapter.dto.request.SignInRequestDto;
import hckt.respalhex.auth.application.dto.response.LogInResponseDto;
import hckt.respalhex.auth.application.port.in.SignInUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class SignInApiV1 {
    private final SignInUseCase signInUseCase;

    @PostMapping("/v1.0.0/signin")
    SignInResponseDto signIn(
            @RequestBody
            SignInRequestDto requestDto
    ) {
        LogInResponseDto logInResponseDto = signInUseCase.signIn(requestDto.convertToApplicationDto());
        return new SignInResponseDto(logInResponseDto.accessToken(), logInResponseDto.refreshToken());
    }
}
