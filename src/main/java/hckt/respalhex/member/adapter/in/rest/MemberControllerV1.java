package hckt.respalhex.member.adapter.in.rest;

import hckt.respalhex.global.dto.ApiCommonResponse;
import hckt.respalhex.member.application.port.dto.request.CreateMemberRequestDto;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1.0.0/member")
public class MemberControllerV1 {
    private final PostMemberUseCase postMemberUseCase;

    @PostMapping
    public ResponseEntity<ApiCommonResponse<String>> create(
            @RequestBody CreateMemberRequestDto requestDto) {
        postMemberUseCase.create(requestDto);
        return ResponseEntity.ok(new ApiCommonResponse<>(true, "회원가입에 성공하였어요."));
    }
}
