package hckt.respalhex.member.adapter.dto.request;

import hckt.respalhex.member.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateMemberAdapterRequestDtoTest {
    @Nested
    @DisplayName("ApplicationDto 변경 테스트")
    class ConvertToApplicationDto {
        private static final String EMAIL = "email";
        private static final String NICKNAME = "nickname";
        private static final String PASSWORD = "password";
        private static final String PICTURE = "picture";
        private static final String PROVIDER = "common";

        @Test
        @DisplayName("정상 케이스")
        void 정상_케이스() {
            // given
            CreateMemberAdapterRequestDto requestDto = CreateMemberAdapterRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER)
                    .build();

            // when
            PostMemberRequestDto applicationDto = requestDto.convertToApplicationDto();

            // then
            assertThat(applicationDto).isNotNull();
            assertThat(applicationDto.email()).isEqualTo(EMAIL);
            assertThat(applicationDto.nickname()).isEqualTo(NICKNAME);
            assertThat(applicationDto.password()).isEqualTo(PASSWORD);
            assertThat(applicationDto.picture()).isEqualTo(PICTURE);
        }

        @Test
        @DisplayName("필수 인자 미입력시 예외 발생 - 이메일")
        void 필수_인자_미입력시_예외_발생_이메일() {
            // given
            CreateMemberAdapterRequestDto requestDto = CreateMemberAdapterRequestDto.builder()
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER)
                    .build();

            // when & then
            assertThatThrownBy(requestDto::convertToApplicationDto)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_EMAIL_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("필수 인자 미입력시 예외 발생 - 비밀번호")
        void 필수_인자_미입력시_예외_발생_비밀번호() {
            // given
            CreateMemberAdapterRequestDto requestDto = CreateMemberAdapterRequestDto.builder()
                    .email(EMAIL)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .provider(PROVIDER)
                    .build();

            // when & then
            assertThatThrownBy(requestDto::convertToApplicationDto)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("필수 인자 미입력시 예외 발생 - 닉네임")
        void 필수_인자_미입력시_예외_발생_닉네임() {
            // given
            CreateMemberAdapterRequestDto requestDto = CreateMemberAdapterRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .picture(PICTURE)
                    .provider(PROVIDER)
                    .build();

            // when & then
            assertThatThrownBy(requestDto::convertToApplicationDto)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_NICKNAME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("필수 인자 미입력시 예외 발생 - provider")
        void 필수_인자_미입력시_예외_발생_provider() {
            // given
            CreateMemberAdapterRequestDto requestDto = CreateMemberAdapterRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .picture(PICTURE)
                    .build();

            // when & then
            assertThatThrownBy(requestDto::convertToApplicationDto)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PROVIDER_TYPE_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("선택 인자 미입력시 정상 생성 - 이미지")
        void 선택_인자_미입력시_정상_생성_이미지() {
            // given
            CreateMemberAdapterRequestDto requestDto = CreateMemberAdapterRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .nickname(NICKNAME)
                    .provider(PROVIDER)
                    .build();

            // when
            PostMemberRequestDto applicationDto = requestDto.convertToApplicationDto();

            // then
            assertThat(applicationDto).isNotNull();
            assertThat(applicationDto.email()).isEqualTo(EMAIL);
            assertThat(applicationDto.nickname()).isEqualTo(NICKNAME);
            assertThat(applicationDto.password()).isEqualTo(PASSWORD);
        }
    }
}
