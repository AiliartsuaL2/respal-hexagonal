package hckt.respalhex.resume.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hckt.respalhex.resume.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ResumeTest {
    private static final String TITLE = "이력서 제목";
    private static final Long MEMBER_ID = 1L;
    private static final Long DIFFERENT_MEMBER_ID = 2L;

    @Nested
    @DisplayName("생성 테스트")
    class Create {
        @Test
        @DisplayName("제목이 없는 경우 예외가 발생한다.")
        void test1() {
            //given
            String title = null;
            Long memberId = MEMBER_ID;

            //when & then
            assertThatThrownBy(() -> new Resume(title, memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("회원 ID가 없는 경우 예외가 발생한다.")
        void test2() {
            //given
            String title = TITLE;
            Long memberId = null;

            //when & then
            assertThatThrownBy(() -> new Resume(title, memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 생성시 조회수가 0이고, 삭제 여부 필드가 false 이다. ")
        void test3() {
            //given
            String title = TITLE;
            Long memberId = MEMBER_ID;

            //when
            Resume resume = new Resume(title, memberId);

            //then
            assertThat(resume.getViews()).isEqualTo(0);
            assertThat(resume.getIsDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("수정 테스트")
    class Update {
        @Test
        @DisplayName("제목이 없는 경우, 예외가 발생한다.")
        void test1() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            String title = null;
            Long memberId = MEMBER_ID;

            //when & then
            assertThatThrownBy(() -> resume.update(title, memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("회원 ID가 없는 경우, 예외가 발생한다.")
        void test2() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            String title = TITLE;
            Long memberId = null;

            //when & then
            assertThatThrownBy(() -> resume.update(title, memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("변경하려는 회원이 이력서를 생성한 회원과 다른 경우 예외가 발생한다.")
        void test3() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            String title = TITLE;
            Long memberId = DIFFERENT_MEMBER_ID;

            //when & then
            assertThatThrownBy(() -> resume.update(title, memberId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.PERMISSION_DENIED_TO_UPDATE.getMessage());
        }

        @Test
        @DisplayName("정상 변경시, 필드가 변경값으로 변경된다.")
        void test4() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            String title = "updatedTitle";
            Long memberId = MEMBER_ID;

            //when
            resume.update(title, memberId);

            //then
            assertThat(resume.getTitle()).isEqualTo(title);
        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    class Delete {
        @Test
        @DisplayName("회원 ID가 없는 경우, 예외가 발생한다.")
        void test1() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            Long memberId = null;

            //when & then
            assertThatThrownBy(() -> resume.delete(memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("삭제하려는 회원이 이력서를 생성한 회원과 다른 경우 예외가 발생한다.")
        void test2() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            Long memberId = DIFFERENT_MEMBER_ID;

            //when & then
            assertThatThrownBy(() -> resume.delete(memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.PERMISSION_DENIED_TO_DELETE.getMessage());
        }

        @Test
        @DisplayName("이미 삭제된 이력서 삭제시 예외가 발생한다.")
        void test3() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            resume.delete(MEMBER_ID);

            //when & then
            assertThatThrownBy(() -> resume.delete(MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 삭제시, 삭제 여부 필드가 true가 되고, 삭제 일시가 설정된다.")
        void test5() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            Long memberId = MEMBER_ID;

            //when
            resume.delete(memberId);

            //then
            assertThat(resume.getIsDeleted()).isTrue();
            assertThat(resume.getDeletedDate()).isNotNull();
        }
    }

    @Nested
    @DisplayName("상세 조회 테스트")
    class View {
        @Test
        @DisplayName("해당 이력서의 주인이 아니고, 태그되지 않은 회원이 조회시 예외가 발생한다.")
        void test1() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            Long notTaggedMemberId = 100L;

            //when & then
            assertThatThrownBy(() -> resume.view(notTaggedMemberId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.PERMISSION_DENIED_TO_VIEW.getMessage());
        }

        @Test
        @DisplayName("본인 이력서의 경우 조회는 성공하지만, 조회수는 증가하지 않는다.")
        void test2() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);

            //when
            resume.view(MEMBER_ID);

            //then
            assertThat(resume.getViews()).isEqualTo(0);
        }

        @Test
        @DisplayName("태그 되어있는 다른 사람이 조회시 해당 이력서의 조회수가 증가한다.")
        void test3() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            Long taggedMemberId = 100L;
            resume.tagMember(taggedMemberId);

            //when
            resume.view(taggedMemberId);

            //then
            assertThat(resume.getViews()).isEqualTo(1);
        }
    }

    class TagMember {
        @Test
        @DisplayName("이미 태그되어있는 회원 태그시 예외가 발생한다")
        void test1() {
            //given
            Resume resume = mock(Resume.class);
            when(resume.isTaggedMember(MEMBER_ID))
                    .thenReturn(true);

            //when & then
            assertThatThrownBy(() -> resume.tagMember(MEMBER_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.ALREADY_TAGGED_MEMBER_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 태그시 해당 이력서 태그 리스트에 해당 회원의 ID가 존재한다.")
        void test2() {
            //given
            Resume resume = new Resume(TITLE, MEMBER_ID);
            Long notTaggedMemberId = 100L;

            //when
            resume.tagMember(notTaggedMemberId);

            //then
            assertThat(resume.isTaggedMember(notTaggedMemberId)).isTrue();

        }


    }
}
