package hckt.respalhex.member.adapter.in.rest;

import hckt.respalhex.member.adapter.dto.request.SignInAdapterRequestDto;
import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import hckt.respalhex.member.exception.OAuthSignInException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
@Slf4j
public class SignInControllerV1 {
    private final SignInUseCase signInUseCase;

    /**
     * 일반 로그인
     * - 로그인시 email, password로 등록 여부 판단
     *   - 등록된 경우
     *      - 회원 ID 포함하여 Auth로 redirect
     *   - 미등록된 경우
     *      - 400 에러 및 에러 메세지 응답
     *
     */
    @PostMapping("/signin")
    ResponseEntity<?> signin(@RequestBody SignInAdapterRequestDto requestDto) {
        Long memberId = signInUseCase.signIn(requestDto.convertToApplicationDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * OAuth 로그인
     * - 로그인시 code, redirectUrl로 이메일 조회
     * - 이메일로 등록 여부 판단
     *   - 등록된 경우
     *     - 회원 ID 포함하여 Auth로 redirect
     *   - 미등록된 경우
     *      - OAuthTmp 저장 후 Uid 포함하여 redirect
     */
    @GetMapping("/{client}/oauth/{provider}")
    ResponseEntity<?> signin(@PathVariable String client, @PathVariable String provider, @RequestParam String code) {
        HttpHeaders headers = new HttpHeaders();
        try {
            Long memberId = signInUseCase.signIn(new OAuthSignInRequestDto(client, provider, code));
            headers.setLocation(URI.create("/"));
        } catch (OAuthSignInException ex) {
            headers.setLocation(ex.getRedirectUri());
        }
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
