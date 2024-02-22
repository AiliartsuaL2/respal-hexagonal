package hckt.respalhex.member.adapter.in.rest;

import hckt.respalhex.global.dto.ApiCommonResponse;
import hckt.respalhex.member.adapter.dto.response.GetMemberResponseDto;
import hckt.respalhex.member.application.dto.request.CreateMemberRequestDto;
import hckt.respalhex.member.application.port.in.GetMemberUseCase;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import hckt.respalhex.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1.0.0/member")
public class MemberControllerV1 {
    private final PostMemberUseCase postMemberUseCase;
    private final GetMemberUseCase getMemberUseCase;

    @PostMapping
    public ResponseEntity<ApiCommonResponse<String>> create(
            @RequestBody CreateMemberRequestDto requestDto) {
        postMemberUseCase.create(requestDto);
        return ResponseEntity.ok(new ApiCommonResponse<>(true, "회원가입에 성공하였어요."));
    }

    @GetMapping
    public ResponseEntity<ApiCommonResponse<GetMemberResponseDto>> get(
            @RequestParam Long id) {
        Member member = getMemberUseCase.getMember(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 회원이 존재하지 않아요"));
        GetMemberResponseDto responseDto = new GetMemberResponseDto(member.email());
        return ResponseEntity.ok(new ApiCommonResponse<>(true, responseDto));
    }
}
