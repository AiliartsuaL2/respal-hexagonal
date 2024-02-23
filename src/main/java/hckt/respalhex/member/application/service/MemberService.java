package hckt.respalhex.member.application.service;

import hckt.respalhex.global.annotation.UseCase;
import hckt.respalhex.global.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.response.GetMemberResponseDto;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.application.port.in.GetMemberUseCase;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import hckt.respalhex.member.domain.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements PostMemberUseCase, GetMemberUseCase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;

    @Override
    @Transactional
    public void create(PostMemberRequestDto requestDto) {
        if(loadMemberPort.loadMemberByEmail(requestDto.email()).isPresent()) {
            throw new IllegalStateException(ErrorMessage.ALREADY_EXIST_MEMBER_EMAIL_EXCEPTION.getMessage());
        }
        Member member = Member.of(requestDto);
        commandMemberPort.create(member);
    }

    @Override
    public GetMemberResponseDto getMember(Long id) {
        Member member = loadMemberPort.loadMember(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage()));
        return new GetMemberResponseDto(member.email());
    }
}

