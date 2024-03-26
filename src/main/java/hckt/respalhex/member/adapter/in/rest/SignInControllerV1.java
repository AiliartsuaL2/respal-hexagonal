package hckt.respalhex.member.adapter.in.rest;

import hckt.respalhex.member.adapter.dto.request.SignInAdapterRequestDto;
import hckt.respalhex.member.application.port.in.SignInUseCase;
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

    // 일반 로그인
    @PostMapping("/signin")
    ResponseEntity<?> signin(@RequestBody SignInAdapterRequestDto requestDto) {
        Long memberId = signInUseCase.signIn(requestDto.convertToApplicationDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    // OAuth 로그인
    @GetMapping("/{client}/oauth/{provider}")
    ResponseEntity<?> signin(@PathVariable String client, @PathVariable String provider, @RequestParam String code) {
        Long memberId = signInUseCase.signIn(client, provider, code);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
