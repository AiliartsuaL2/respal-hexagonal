package hckt.respalhex.auth.adapter.in.rest;

import hckt.respalhex.auth.adapter.dto.response.SignInResponseDto;
import hckt.respalhex.auth.adapter.dto.request.SignInRequestDto;
import hckt.respalhex.auth.application.dto.response.LogInResponseDto;
import hckt.respalhex.auth.application.port.in.SignInUseCase;
import hckt.respalhex.global.dto.ApiCommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1.0")
class SignInApiV1 {
    private final SignInUseCase signInUseCase;

    @PostMapping("/sign-in")
    ApiCommonResponse<SignInResponseDto> signIn(
            @RequestBody
            SignInRequestDto requestDto
    ) {
        LogInResponseDto logInResponseDto = signInUseCase.signIn(requestDto.convertToApplicationDto());
        return new ApiCommonResponse<>(true, new SignInResponseDto(logInResponseDto.accessToken(), logInResponseDto.refreshToken()));
    }

    @GetMapping("/{client}/oauth/{provider}")
    ApiCommonResponse<SignInResponseDto> oAuthSignIn(
            @PathVariable String client,
            @PathVariable String provider,
            @RequestParam String code){
        LogInResponseDto logInResponseDto = signInUseCase.signIn(client, provider, code);
        return new ApiCommonResponse<>(true, new SignInResponseDto(logInResponseDto.accessToken(), logInResponseDto.refreshToken()));
    }
}
