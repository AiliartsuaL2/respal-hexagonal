package hckt.respalhex.resume.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hckt.respalhex.resume.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TagTest {
    private static final Long MEMBER_ID = 1L;
    private static final Long RESUME_ID = 0L;
    private static final Resume RESUME = mock(Resume.class);

    @Nested
    @DisplayName("생성 테스트")
    class Create {
        @Test
        @DisplayName("태그하는 대상이 이력서의 주인인 경우 예외가 발생한다.")
        void test1() {
            //given
            when(RESUME.isOwner(MEMBER_ID))
                    .thenReturn(true);

            //when
            assertThatThrownBy(() -> new Tag(RESUME, MEMBER_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.CAN_NOT_TAG_OWN_RESUME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("태그하는 대상이 이미 태그되어있는 경우 예외가 발생한다.")
        void test2() {
            //given
            when(RESUME.isOwner(MEMBER_ID))
                    .thenReturn(false);
            when(RESUME.isTaggedMember(MEMBER_ID))
                    .thenReturn(true);

            //when
            assertThatThrownBy(() -> new Tag(RESUME, MEMBER_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.ALREADY_TAGGED_MEMBER_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 태그 생성시 필드가 지정한 값으로 매핑된다.")
        void test3() {
            //given
            when(RESUME.getId())
                    .thenReturn(RESUME_ID);
            when(RESUME.isOwner(MEMBER_ID))
                    .thenReturn(false);
            when(RESUME.isTaggedMember(MEMBER_ID))
                    .thenReturn(false);

            //when
            Tag tag = new Tag(RESUME, MEMBER_ID);

            //then
            assertThat(tag.getResume()).isEqualTo(RESUME);
            assertThat(tag.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(tag.getResumeId()).isEqualTo(RESUME_ID);
        }
    }
}
