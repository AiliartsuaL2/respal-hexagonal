package hckt.respalhex.resume.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import hckt.respalhex.resume.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ResumeFileTest {
    private static final String ORIGIN_NAME = "원래 파일 명";
    private static final String REGISTERED_NAME = "저장된 파일 명";
    private static final String ACCESS_URL = "접근 URL";
    private static final Long RESUME_OWNER = 1L;
    private static final Long NOT_RESUME_OWNER = 2L;
    private static final Long RESUME_ID = 1L;
    private static final Resume RESUME = spy(new Resume("제", RESUME_OWNER));

    @BeforeAll
    static void init() {
        when(RESUME.getId())
                .thenReturn(RESUME_ID);
    }

    @Nested
    @DisplayName("생성 테스트")
    class create {
        @Test
        @DisplayName("기존 파일명 미존재시 예외가 발생한다.")
        void test1() {
            //given
            String originName = null;

            //when & then
            assertThatThrownBy(() -> new ResumeFile(originName, REGISTERED_NAME, ACCESS_URL, RESUME))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_ORIGIN_NAME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("저장 파일명 미존재시 예외가 발생한다.")
        void test2() {
            //given
            String registeredName = null;

            //when & then
            assertThatThrownBy(() -> new ResumeFile(ORIGIN_NAME, registeredName, ACCESS_URL, RESUME))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_REGISTERED_NAME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("접근 URL 미존재시 예외가 발생한다.")
        void test3() {
            //given
            String accessUrl = null;

            //when & then
            assertThatThrownBy(() -> new ResumeFile(ORIGIN_NAME, REGISTERED_NAME, accessUrl, RESUME))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_ACCESS_URL_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("이력서 미존재시 예외가 발생한다.")
        void test4() {
            //given
            Resume resume = null;

            //when & then
            assertThatThrownBy(() -> new ResumeFile(ORIGIN_NAME, REGISTERED_NAME, ACCESS_URL, resume))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 생성시 입력 필드가 매핑되고, 삭제 여부 상태가 false 이다.")
        void test5() {
            //given & when
            ResumeFile resumeFile = new ResumeFile(ORIGIN_NAME, REGISTERED_NAME, ACCESS_URL, RESUME);

            //then
            assertThat(resumeFile.getOriginName()).isEqualTo(ORIGIN_NAME);
            assertThat(resumeFile.getRegisteredName()).isEqualTo(REGISTERED_NAME);
            assertThat(resumeFile.getAccessUrl()).isEqualTo(ACCESS_URL);
            assertThat(resumeFile.getResumeId()).isEqualTo(RESUME_ID);
            assertThat(resumeFile.getIsDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    class delete {
        @Test
        @DisplayName("회원 ID 미존재시 예외가 발생한다.")
        void test1() {
            //given
            ResumeFile resumeFile = new ResumeFile(ORIGIN_NAME, REGISTERED_NAME, ACCESS_URL, RESUME);
            Long memberId = null;

            //when & then
            assertThatThrownBy(() -> resumeFile.delete(memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("삭제의 주체가 이력서의 주인이 아닌경우 예외가 발생한다.")
        void test2() {
            //given
            ResumeFile resumeFile = new ResumeFile(ORIGIN_NAME, REGISTERED_NAME, ACCESS_URL, RESUME);

            //when & then
            assertThatThrownBy(() -> resumeFile.delete(NOT_RESUME_OWNER))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.PERMISSION_DENIED_TO_DELETE.getMessage());
        }

        @Test
        @DisplayName("정상 삭제시 삭제 여부 필드가 true가 되고, 삭제 시간이 설정된다.")
        void test3() {
            //given
            ResumeFile resumeFile = new ResumeFile(ORIGIN_NAME, REGISTERED_NAME, ACCESS_URL, RESUME);

            //when
            resumeFile.delete(RESUME_OWNER);

            //when
            assertThat(resumeFile.getIsDeleted()).isTrue();
            assertThat(resumeFile.getDeletedDate()).isNotNull();

        }
    }
}
