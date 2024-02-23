package hckt.respalhex.member.application.service;

import hckt.respalhex.global.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import hckt.respalhex.member.application.port.out.CommandMemberPort;
import hckt.respalhex.member.application.port.out.LoadMemberPort;
import hckt.respalhex.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    private final LoadMemberPort loadMemberPort = mock(LoadMemberPort.class);
    private final CommandMemberPort commandMemberPort = mock(CommandMemberPort.class);
    private final MemberService memberService = new MemberService(loadMemberPort, commandMemberPort);

    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String PICTURE = "picture";

    @Nested
    @DisplayName("회원 가입 테스트")
    class Create {
        @Test
        @DisplayName("정상 케이스")
        void 정상_케이스() {
            // given
            PostMemberRequestDto requestDto = PostMemberRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .build();

            // when
            memberService.create(requestDto);

            // then
            then(commandMemberPort).should(times(1)).create(any());
        }

        @Test
        @DisplayName("동일한 이메일로 가입한 회원인 경우 예외 발생")
        void 동일한_이메일로_가입한_회원인_경우_예외_발생() {
            // given
            when(loadMemberPort.loadMemberByEmail(EMAIL))
                    .thenReturn(Optional.of(new Member(null, EMAIL, PASSWORD, NICKNAME, PICTURE)));
            PostMemberRequestDto requestDto = PostMemberRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .build();

            // when & then
            assertThatThrownBy(() -> memberService.create(requestDto))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.ALREADY_EXIST_MEMBER_EMAIL_EXCEPTION.getMessage());
        }
    }
}