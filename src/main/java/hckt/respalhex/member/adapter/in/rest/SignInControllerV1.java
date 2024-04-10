package hckt.respalhex.member.adapter.in.rest;

import hckt.respalhex.member.adapter.dto.request.SignInAdapterRequestDto;
import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.application.dto.response.GetTokenResponseDto;
import hckt.respalhex.member.application.port.in.GetTokenUseCase;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import hckt.respalhex.member.exception.OAuthSignInException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1.0")
@Slf4j
public class SignInControllerV1 {
    private final SignInUseCase signInUseCase;
    private final GetTokenUseCase getTokenUseCase;
    private final String webUrl;
    private final String appCustomScheme;

    private final String webLoginPath;
    private final String appLoginPath;

    private final String webSignUpPath;
    private final String appSignUpPath;

    public SignInControllerV1(SignInUseCase signInUseCase, GetTokenUseCase getTokenUseCase,
                              @Value("${respal.web.url}") String webUrl, @Value("${respal.app.custom-scheme}") String appCustomScheme,
                              @Value("${respal.web.path.login}") String webLoginPath, @Value("${respal.app.path.login}") String appLoginPath,
                              @Value("${respal.web.path.sign-up}") String webSignUpPath, @Value("${respal.app.path.sign-up}") String appSignUpPath) {
        this.signInUseCase = signInUseCase;
        this.getTokenUseCase = getTokenUseCase;
        this.webUrl = webUrl;
        this.appCustomScheme = appCustomScheme;
        this.webLoginPath = webLoginPath;
        this.appLoginPath = appLoginPath;
        this.webSignUpPath = webSignUpPath;
        this.appSignUpPath = appSignUpPath;
    }

    /**
     * 일반 로그인
     * - 로그인시 email, password로 등록 여부 판단
     *   - 등록된 경우
     *      - * Not use 회원 ID 포함하여 Auth로 redirect
     *      - 회원 ID를 이용하여 SQS 통신하여 토큰을 받아 Client에 맞게 Redirect
     *   - 미등록된 경우 (로그인 실패시)
     *      - 400 에러 및 에러 메세지 응답
     *
     */
    @PostMapping("/signin")
    ResponseEntity<?> signin(@RequestBody SignInAdapterRequestDto requestDto) {
        Long memberId = signInUseCase.signIn(requestDto.convertToApplicationDto());
        GetTokenResponseDto token = getTokenUseCase.getToken(memberId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(getLoginUri(requestDto.client(), token));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * OAuth 로그인
     * - 로그인시 code, redirectUrl로 이메일 조회
     * - 이메일로 등록 여부 판단
     *   - 등록된 경우
     *      - * Not use 회원 ID 포함하여 Auth로 redirect
     *      - 회원 ID를 이용하여 SQS 통신하여 토큰을 받아 Client에 맞게 Redirect
     *   - 미등록된 경우
     *      - OAuthTmp 저장 후 Uid 포함하여 Web url로 redirect
     */
    @GetMapping("/{client}/oauth/{provider}")
    ResponseEntity<?> signin(@PathVariable String client, @PathVariable String provider, @RequestParam String code) {
        HttpHeaders headers = new HttpHeaders();
        try {
            Long memberId = signInUseCase.signIn(new OAuthSignInRequestDto(client, provider, code));
            GetTokenResponseDto token = getTokenUseCase.getToken(memberId);
            headers.setLocation(getLoginUri(client, token));
        } catch (OAuthSignInException ex) {
            String uidParameter = ex.getUidParameter();
            headers.setLocation(getSignUpUri(client, uidParameter));
        }
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    private URI getLoginUri(String client, GetTokenResponseDto token) {
        if ("app".equals(client)) {
            return URI.create(appCustomScheme + appLoginPath + token.convertToQueryParam());
        }
        return URI.create(webUrl + webLoginPath + token.convertToQueryParam());
    }

    private URI getSignUpUri(String client, String parameter) {
        if ("app".equals(client)) {
            return URI.create(appCustomScheme + appSignUpPath + parameter);
        }
        return URI.create(webUrl + webSignUpPath + parameter);
    }
}
