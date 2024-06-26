package hckt.respalhex.member.adapter.in.rest;

import hckt.respalhex.global.dto.ApiCommonResponse;
import hckt.respalhex.member.adapter.dto.request.CreateMemberAdapterRequestDto;
import hckt.respalhex.member.application.port.in.GetMemberUseCase;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1.0")
class MemberControllerV1 {
    private final PostMemberUseCase postMemberUseCase;
    private final GetMemberUseCase getMemberUseCase;

    @PostMapping("/sign-up")
    ResponseEntity<ApiCommonResponse<String>> create(
            @RequestBody CreateMemberAdapterRequestDto requestDto) {
        postMemberUseCase.create(requestDto.convertToApplicationDto());
        return new ResponseEntity<>(new ApiCommonResponse<>(true, "회원가입에 성공하였어요."), HttpStatus.CREATED);
    }
}
