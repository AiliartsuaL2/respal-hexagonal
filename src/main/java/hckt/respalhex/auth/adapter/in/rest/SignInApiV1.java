package hckt.respalhex.auth.adapter.in.rest;

import hckt.respalhex.auth.adapter.dto.response.SignInResponseDto;
import hckt.respalhex.auth.adapter.dto.request.SignInRequestDto;
import hckt.respalhex.auth.application.dto.response.LogInResponseDto;
import hckt.respalhex.auth.application.port.in.SignInUseCase;
import hckt.respalhex.global.dto.ApiCommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class SignInApiV1 {
    private final SignInUseCase signInUseCase;

    @PostMapping("/v1.0.0/signin")
    ApiCommonResponse<SignInResponseDto> signIn(
            @RequestBody
            SignInRequestDto requestDto
    ) {
        LogInResponseDto logInResponseDto = signInUseCase.signIn(requestDto.convertToApplicationDto());
        return new ApiCommonResponse<>(true, new SignInResponseDto(logInResponseDto.accessToken(), logInResponseDto.refreshToken()));
    }

    @GetMapping("/v1.0.0/oauth/{client}/login/{provider}")
    ApiCommonResponse<SignInResponseDto> oAuthSignIn(
            @PathVariable String client,
            @PathVariable String provider,
            @RequestParam String code){
        LogInResponseDto logInResponseDto = signInUseCase.signIn(client, provider, code);
        return new ApiCommonResponse<>(true, new SignInResponseDto(logInResponseDto.accessToken(), logInResponseDto.refreshToken()));
    }
}
