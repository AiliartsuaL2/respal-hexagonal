package hckt.respalhex.member.application.service;

import hckt.respalhex.global.event.CreateUserAccountEvent;
import hckt.respalhex.member.application.port.out.CommandOAuthPort;
import hckt.respalhex.member.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import hckt.respalhex.member.domain.Member;
import hckt.respalhex.member.domain.converter.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@Transactional
class MemberServiceTest {
    private final LoadMemberPort loadMemberPort = mock(LoadMemberPort.class);
    private final CommandMemberPort commandMemberPort = mock(CommandMemberPort.class);
    private final CommandOAuthPort commandOAuthPort = mock(CommandOAuthPort.class);
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final MemberService memberService = new MemberService(loadMemberPort, commandMemberPort, commandOAuthPort, applicationEventPublisher);

    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String PICTURE = "picture";
    private static final String PROVIDER_COMMON = "common";
    private static final String PROVIDER_KAKAO = "common";

    @Nested
    @DisplayName("회원 가입 테스트")
    class Create {
        @Test
        @DisplayName("정상 케이스")
        void test1() {
            // given
            PostMemberRequestDto requestDto = PostMemberRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER_COMMON)
                    .build();

            // when
            memberService.create(requestDto);

            // then
            then(commandMemberPort).should(times(1)).create(any());
        }

        @Test
        @DisplayName("동일한 이메일, 다른 provider 가입한 회원인 경우 정상 생성")
        void test2() {
            // given
            PostMemberRequestDto requestDto = PostMemberRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER_COMMON)
                    .build();
            memberService.create(requestDto);

            PostMemberRequestDto requestDto2 = PostMemberRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER_KAKAO)
                    .build();
            // when
            memberService.create(requestDto2);

            // then
            then(commandMemberPort).should(times(2)).create(any());
        }

        @Test
        @DisplayName("동일한 이메일, 동일한 provider 가입한 회원인 경우 예외 발생")
        void test3() {
            // given
            PostMemberRequestDto requestDto = PostMemberRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER_COMMON)
                    .build();
            Member member = Member.create(requestDto);

            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.COMMON))
                    .thenReturn(Optional.of(member));

            // when & then
            assertThatThrownBy(() -> memberService.create(requestDto))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.ALREADY_EXIST_MEMBER_EMAIL_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("회원 생성시 이벤트 발행")
        void test4() {
            // given
            PostMemberRequestDto requestDto = PostMemberRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER_COMMON)
                    .build();

            // when
            memberService.create(requestDto);

            // then
            then(applicationEventPublisher).should(times(1)).publishEvent(isA(CreateUserAccountEvent.class));
        }
    }
}
