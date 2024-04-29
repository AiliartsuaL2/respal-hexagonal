package hckt.respalhex.resume.application.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import hckt.respalhex.resume.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateResumeRequestDtoTest {
    private static final String TITLE = "이력서 제목";
    private static final Long MEMBER_ID = 1L;
    @Test
    @DisplayName("제목이 없는 경우 예외가 발생한다.")
    void test1() {
        //given
        String title = null;

        //when & then
        Assertions.assertThatThrownBy(() -> new CreateResumeRequestDto(title, MEMBER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("회원 ID가 없는 경우 예외가 발생한다.")
    void test2() {
        //given
        Long memberId = null;

        //when & then
        Assertions.assertThatThrownBy(() -> new CreateResumeRequestDto(TITLE, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("정상 생성시 필드가 지정한 값으로 매핑된다.")
    void test3() {
        //given & when
        CreateResumeRequestDto requestDto = new CreateResumeRequestDto(TITLE, MEMBER_ID);

        //then
        Assertions.assertThat(requestDto.title()).isEqualTo(TITLE);
        Assertions.assertThat(requestDto.memberId()).isEqualTo(MEMBER_ID);
    }
}
