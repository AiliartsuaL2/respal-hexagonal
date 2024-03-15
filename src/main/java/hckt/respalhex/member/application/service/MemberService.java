package hckt.respalhex.member.application.service;

import hckt.respalhex.global.annotation.UseCase;
import hckt.respalhex.global.event.CreateUserAccountEvent;
import hckt.respalhex.member.application.port.in.SignInUseCase;
import hckt.respalhex.member.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.response.GetMemberResponseDto;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.application.port.in.GetMemberUseCase;
import hckt.respalhex.member.application.port.in.PostMemberUseCase;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import hckt.respalhex.member.domain.Member;
import hckt.respalhex.member.domain.OAuth;
import hckt.respalhex.member.domain.converter.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
class MemberService implements PostMemberUseCase, GetMemberUseCase, SignInUseCase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void create(PostMemberRequestDto requestDto) {
        Provider provider = Provider.findByValue(requestDto.provider());
        if(loadMemberPort.loadMemberByEmailAndProvider(requestDto.email(), provider).isPresent()) {
            throw new IllegalStateException(ErrorMessage.ALREADY_EXIST_MEMBER_EMAIL_EXCEPTION.getMessage());
        }

        Member member = Member.create(requestDto);
        OAuth oAuth = OAuth.create(member, provider);
        member.addOAuth(oAuth);

        commandMemberPort.create(member);
        eventPublisher.publishEvent(new CreateUserAccountEvent(member.getId(), "user"));
    }

    @Override
    public GetMemberResponseDto getMember(Long id) {
        Member member = loadMemberPort.loadMember(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage()));
        return new GetMemberResponseDto(member.getEmail());
    }

    @Override
    public Long signIn(String email, String password) {
        Member member = loadMemberPort.loadMemberByEmailAndProvider(email, Provider.COMMON)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage()));
        if (member.matchPassword(password)) {
            return member.getId();
        }
        return null;
    }
}

