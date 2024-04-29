package hckt.respalhex.resume.application.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import hckt.respalhex.resume.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UpdateResumeRequestDtoTest {
    private static final Long RESUME_ID = 0L;
    private static final String TITLE = "이력서 제목";
    private static final Long MEMBER_ID = 1L;

    @Test
    @DisplayName("이력서 ID가 없는 경우 예외가 발생한다.")
    void test1() {
        //given
        Long resumeId = null;

        //when & then
        Assertions.assertThatThrownBy(() -> new UpdateResumeRequestDto(resumeId, TITLE, MEMBER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_RESUME_ID_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("제목이 없는 경우 예외가 발생한다.")
    void test2() {
        //given
        String title = null;

        //when & then
        Assertions.assertThatThrownBy(() -> new UpdateResumeRequestDto(RESUME_ID, title, MEMBER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("회원 ID가 없는 경우 예외가 발생한다.")
    void test3() {
        //given
        Long memberId = null;

        //when & then
        Assertions.assertThatThrownBy(() -> new UpdateResumeRequestDto(RESUME_ID, TITLE, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("정상 생성시 필드가 지정한 값으로 매핑된다.")
    void test4() {
        //given & when
        UpdateResumeRequestDto requestDto = new UpdateResumeRequestDto(RESUME_ID, TITLE, MEMBER_ID);

        //then
        Assertions.assertThat(requestDto.resumeId()).isEqualTo(RESUME_ID);
        Assertions.assertThat(requestDto.title()).isEqualTo(TITLE);
        Assertions.assertThat(requestDto.memberId()).isEqualTo(MEMBER_ID);
    }
}
