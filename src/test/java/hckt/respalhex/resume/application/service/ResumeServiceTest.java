package hckt.respalhex.resume.application.service;

import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3Client;
import hckt.respalhex.resume.application.dto.request.CreateResumeRequestDto;
import hckt.respalhex.resume.application.port.out.CommandResumeFilePort;
import hckt.respalhex.resume.application.port.out.CommandResumePort;
import hckt.respalhex.resume.application.port.out.LoadResumeFilePort;
import hckt.respalhex.resume.application.port.out.LoadResumePort;
import hckt.respalhex.resume.domain.Resume;
import hckt.respalhex.resume.domain.ResumeFile;
import hckt.respalhex.resume.exception.ErrorMessage;
import hckt.respalhex.resume.exception.MultipartException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class ResumeServiceTest {
    private static final String BUCKET_NAME = "bucketName";
    private static final String TITLE = "title";
    private static final String FILE_PATH = "filePath";
    private static final String FILE_NAME = "fileName.jpg";
    private static final Long MEMBER_ID = 1L;

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
        @Test
        @DisplayName("파일명에 확장자가 없는경우 예외가 발생한다.")
        void test1() {
            //given
            String notHasExtensionFileName = "fileName";
            CreateResumeRequestDto requestDto = new CreateResumeRequestDto(TITLE, FILE_PATH,
                    notHasExtensionFileName, MEMBER_ID);

            //when & then
            Assertions.assertThatThrownBy(() -> resumeService.create(requestDto, multipartFile))
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
            Assertions.assertThatThrownBy(() -> resumeService.create(requestDto, multipartFile))
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
}
