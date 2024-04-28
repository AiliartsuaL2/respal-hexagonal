package hckt.respalhex.resume.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3Client;
import hckt.respalhex.resume.application.dto.request.CreateResumeRequestDto;
import hckt.respalhex.resume.application.dto.request.UpdateResumeRequestDto;
import hckt.respalhex.resume.application.port.out.CommandResumeFilePort;
import hckt.respalhex.resume.application.port.out.CommandResumePort;
import hckt.respalhex.resume.application.port.out.LoadResumeFilePort;
import hckt.respalhex.resume.application.port.out.LoadResumePort;
import hckt.respalhex.resume.domain.MemberInfo;
import hckt.respalhex.resume.domain.Resume;
import hckt.respalhex.resume.domain.ResumeFile;
import hckt.respalhex.resume.exception.ErrorMessage;
import hckt.respalhex.resume.exception.MultipartException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class ResumeServiceTest {
    private static final String BUCKET_NAME = "bucketName";

    private final MultipartFile multipartFile = mock(MultipartFile.class);
    private final CommandResumePort commandResumePort = mock(CommandResumePort.class);
    private final LoadResumePort loadResumePort = mock(LoadResumePort.class);
    private final LoadResumeFilePort loadResumeFilePort = mock(LoadResumeFilePort.class);
    private final CommandResumeFilePort commandResumeFilePort = mock(CommandResumeFilePort.class);
    private final AmazonS3Client amazonS3Client = mock(AmazonS3Client.class);

    ResumeService resumeService = new ResumeService(commandResumePort, loadResumePort,
            loadResumeFilePort, commandResumeFilePort, amazonS3Client, BUCKET_NAME);

    @Nested
    @DisplayName("이력서 생성 테스트")
    class Create {
        private static final String TITLE = "title";
        private static final String FILE_PATH = "filePath";
        private static final String FILE_NAME = "fileName.jpg";
        private static final Long MEMBER_ID = 1L;
        @Test
        @DisplayName("파일명에 확장자가 없는경우 예외가 발생한다.")
        void test1() {
            //given
            String notHasExtensionFileName = "fileName";
            CreateResumeRequestDto requestDto = new CreateResumeRequestDto(TITLE, FILE_PATH,
                    notHasExtensionFileName, MEMBER_ID);

            //when & then
            assertThatThrownBy(() -> resumeService.create(requestDto, multipartFile))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_EXTENSION_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("파일 등록시, IOException이 발생하는경우 MultipartException이 발생한다.")
        void test2() throws IOException {
            //given
            CreateResumeRequestDto requestDto = new CreateResumeRequestDto(TITLE, FILE_PATH,
                    FILE_NAME, MEMBER_ID);
            when(multipartFile.getInputStream())
                    .thenThrow(IOException.class);

            //when & then
            assertThatThrownBy(() -> resumeService.create(requestDto, multipartFile))
                    .isInstanceOf(MultipartException.class)
                    .hasMessage(ErrorMessage.REGISTER_MULTIPART_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 등록시 이력서와 이력서 파일을 저장한다.")
        void test3() throws IOException {
            //given
            CreateResumeRequestDto requestDto = new CreateResumeRequestDto(TITLE, FILE_PATH,
                    FILE_NAME, MEMBER_ID);

            URL url = mock(URL.class);
            String accessUrl = "accessUrl";
            when(url.toString()).thenReturn(accessUrl);
            when(amazonS3Client.getUrl(any(), any()))
                    .thenReturn(url);

            InputStream inputStream = mock(InputStream.class);
            int available = 1;
            when(inputStream.available())
                    .thenReturn(available);
            when(multipartFile.getInputStream())
                    .thenReturn(inputStream);

            //when
            resumeService.create(requestDto, multipartFile);

            // then
            then(commandResumePort).should(times(1)).create(any(Resume.class));
            then(commandResumeFilePort).should(times(1)).create(any(ResumeFile.class));
        }
    }

    @Nested
    @DisplayName("이력서 삭제 테스트")
    class Delete {
        private static final Long EXIST_RESUME_ID = 1L;
        private static final Long NOT_EXIST_RESUME_ID = 0L;
        private static final Long MEMBER_ID = 1L;

        @Test
        @DisplayName("이력서 id 미존재시 예외가 발생한다")
        void test1() {
            //given
            Long resumeId = null;

            //when & then
            assertThatThrownBy(() -> resumeService.delete(resumeId, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_RESUME_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("회원 id 미존재시 예외가 발생한다")
        void test2() {
            //given
            Long memberId = null;

            //when & then
            assertThatThrownBy(() -> resumeService.delete(EXIST_RESUME_ID, memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("이력서 id에 해당하는 이력서 미존재시 예외가 발생한다")
        void test3() {
            //given
            when(loadResumePort.findById(NOT_EXIST_RESUME_ID))
                    .thenReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> resumeService.delete(NOT_EXIST_RESUME_ID, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 삭제시 delete 메서드가 실행된다")
        void test4() {
            //given
            Resume resume = mock(Resume.class);
            when(loadResumePort.findById(EXIST_RESUME_ID))
                    .thenReturn(Optional.of(resume));

            //when
            resumeService.delete(EXIST_RESUME_ID, MEMBER_ID);

            //then
            then(resume).should(times(1)).delete(MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("이력서 수정 테스트")
    class Update {
        private static final Long EXIST_RESUME_ID = 1L;
        private static final Long NOT_EXIST_RESUME_ID = 0L;
        private static final Long MEMBER_ID = 1L;
        private static final String UPDATED_TITLE = "수정된 이력서 제목";

        @Test
        @DisplayName("이력서 id에 해당하는 이력서 미존재시 예외가 발생한다")
        void test1() {
            //given
            when(loadResumePort.findById(NOT_EXIST_RESUME_ID))
                    .thenReturn(Optional.empty());
            UpdateResumeRequestDto requestDto = new UpdateResumeRequestDto(NOT_EXIST_RESUME_ID, UPDATED_TITLE, MEMBER_ID);

            //when & then
            assertThatThrownBy(() -> resumeService.update(requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 수정 요청시 update 메서드가 실행된다")
        void test4() {
            //given
            Resume resume = mock(Resume.class);
            when(loadResumePort.findById(EXIST_RESUME_ID))
                    .thenReturn(Optional.of(resume));
            UpdateResumeRequestDto requestDto = new UpdateResumeRequestDto(EXIST_RESUME_ID, UPDATED_TITLE, MEMBER_ID);

            //when
            resumeService.update(requestDto);

            //then
            then(resume).should(times(1)).update(UPDATED_TITLE, MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("이력서 상세 조회 테스트")
    class View {
        private static final Long EXIST_RESUME_ID = 1L;
        private static final Long NOT_EXIST_RESUME_ID = 0L;
        private static final Long MEMBER_ID = 1L;

        @Test
        @DisplayName("이력서 id 미존재시 예외가 발생한다")
        void test1() {
            //given
            Long resumeId = null;

            //when & then
            assertThatThrownBy(() -> resumeService.view(resumeId, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_RESUME_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("조회자 id 미존재시 예외가 발생한다")
        void test2() {
            //given
            Long memberId = null;

            //when & then
            assertThatThrownBy(() -> resumeService.view(EXIST_RESUME_ID, memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("이력서 id에 해당하는 이력서 미존재시 예외가 발생한다")
        void test3() {
            //given
            when(loadResumePort.findResumeWithMemberInfo(NOT_EXIST_RESUME_ID))
                    .thenReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> resumeService.view(NOT_EXIST_RESUME_ID, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_RESUME_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("정상 상세 조회 요청시 view() 메서드가 실행된다.")
        void test4() {
            //given
            Resume resume = mock(Resume.class);
            MemberInfo memberInfo = mock(MemberInfo.class);

            when(resume.getMemberInfo())
                    .thenReturn(memberInfo);
            when(loadResumePort.findResumeWithMemberInfo(EXIST_RESUME_ID))
                    .thenReturn(Optional.of(resume));

            //when
            resumeService.view(EXIST_RESUME_ID, MEMBER_ID);

            //then
            then(resume).should(times(1)).view();
        }
    }
}
