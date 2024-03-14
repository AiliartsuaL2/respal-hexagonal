package hckt.respalhex.member.adapter.in.rest;

import hckt.respalhex.global.dto.ApiCommonResponse;
import hckt.respalhex.member.adapter.dto.request.CreateMemberRequestDto;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class CreateMemberControllerV1 {
    private final PostMemberUseCase postMemberUseCase;

    @PostMapping("/v1.0.0/member")
    ResponseEntity<ApiCommonResponse<String>> create(
            @RequestBody CreateMemberRequestDto requestDto) {
        postMemberUseCase.create(requestDto.convertToApplicationDto());
        return new ResponseEntity<>(new ApiCommonResponse<>(true, "회원가입에 성공하였어요."), HttpStatus.CREATED);
    }
}
