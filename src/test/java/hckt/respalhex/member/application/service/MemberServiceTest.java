package hckt.respalhex.member.application.service;

import hckt.respalhex.global.event.CreateUserAccountEvent;
import hckt.respalhex.member.application.dto.request.OAuthSignInRequestDto;
import hckt.respalhex.member.application.dto.request.SignInMemberRequestDto;
import hckt.respalhex.member.application.port.out.*;
import hckt.respalhex.member.domain.OAuthInfo;
import hckt.respalhex.member.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.domain.Member;
import hckt.respalhex.member.domain.converter.Provider;
import hckt.respalhex.member.exception.OAuthSignInException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@Transactional
class MemberServiceTest {
    private final LoadMemberPort loadMemberPort = mock(LoadMemberPort.class);
    private final CommandMemberPort commandMemberPort = mock(CommandMemberPort.class);
    private final LoadOAuthInfoPort loadOAuthInfoPort = mock(LoadOAuthInfoPort.class);
    private final CommandOAuthInfoPort commandOAuthInfoPort = mock(CommandOAuthInfoPort.class);
    private final CommandOAuthPort commandOAuthPort = mock(CommandOAuthPort.class);

    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final MemberService memberService = new MemberService(loadMemberPort, commandMemberPort, loadOAuthInfoPort, commandOAuthInfoPort, commandOAuthPort, applicationEventPublisher);

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

    @Nested
    @DisplayName("일반 로그인 테스트")
    class CommonSignIn {
        @Test
        @DisplayName("이메일에 해당하는 회원이 존재하지 않는 경우, 예외가 발생한다.")
        void test1() {
            //given
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.COMMON))
                    .thenReturn(Optional.empty());
            SignInMemberRequestDto signInMemberRequestDto = new SignInMemberRequestDto(EMAIL, PASSWORD);

            //when & then
            assertThatThrownBy(() -> memberService.signIn(signInMemberRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않는 경우, 예외가 발생한다.")
        void test2() {
            //given
            Member member = mock(Member.class);
            when(member.matchPassword(PASSWORD)).thenReturn(false);
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.COMMON))
                    .thenReturn(Optional.of(member));
            SignInMemberRequestDto signInMemberRequestDto = new SignInMemberRequestDto(EMAIL, PASSWORD);

            //when & then
            assertThatThrownBy(() -> memberService.signIn(signInMemberRequestDto))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.NOT_MATCH_PASSWORD_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("로그인 성공시 회원 ID을 반환한다.")
        void test3() {
            //given
            Long id = 0L;
            Member member = mock(Member.class);
            when(member.matchPassword(PASSWORD)).thenReturn(true);
            when(member.getId()).thenReturn(id);
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.COMMON))
                    .thenReturn(Optional.of(member));
            SignInMemberRequestDto signInMemberRequestDto = new SignInMemberRequestDto(EMAIL, PASSWORD);

            //when
            Long result = memberService.signIn(signInMemberRequestDto);

            //then
            assertThat(result).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("소셜 로그인 테스트")
    class OAuthSignIn {
        @Test
        @DisplayName("가입되지 않은 회원인 경우 소셜 정보 저장 후 예외 발생")
        void test1() {
            //given
            String client = "web";
            String code = "code";
            Long id = 0L;

            OAuthSignInRequestDto oAuthSignInRequestDto = new OAuthSignInRequestDto(client, PROVIDER_KAKAO, code);
            OAuthInfo oAuthInfo = mock(OAuthInfo.class);
            Member member = mock(Member.class);

            when(member.getId()).thenReturn(id);
            when(oAuthInfo.getEmail()).thenReturn(EMAIL);
            when(loadOAuthInfoPort.loadOAuthInfo(oAuthSignInRequestDto))
                    .thenReturn(oAuthInfo);
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.KAKAO))
                    .thenReturn(Optional.of(member));

            //when & then
            assertThatThrownBy(() -> memberService.signIn(oAuthSignInRequestDto))
                    .isInstanceOf(OAuthSignInException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_EXCEPTION.getMessage());
            then(commandOAuthInfoPort).should(times(1)).save(oAuthInfo);
        }

        @Test
        @DisplayName("로그인 성공시 회원 ID를 반환한다.")
        void test2() {
            //given
            String client = "web";
            String code = "code";
            Long id = 0L;

            OAuthSignInRequestDto oAuthSignInRequestDto = new OAuthSignInRequestDto(client, PROVIDER_KAKAO, code);
            OAuthInfo oAuthInfo = mock(OAuthInfo.class);
            Member member = mock(Member.class);

            when(member.getId()).thenReturn(id);
            when(loadOAuthInfoPort.loadOAuthInfo(oAuthSignInRequestDto))
                    .thenReturn(oAuthInfo);
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.KAKAO))
                    .thenReturn(Optional.of(member));

            //when
            Long result = memberService.signIn(oAuthSignInRequestDto);

            //then
            assertThat(result).isEqualTo(id);
        }
    }
}
