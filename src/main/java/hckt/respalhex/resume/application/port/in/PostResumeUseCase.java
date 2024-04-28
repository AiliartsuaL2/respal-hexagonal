package hckt.respalhex.resume.application.port.in;

import hckt.respalhex.resume.application.dto.request.CreateResumeRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface PostResumeUseCase {
    void create(CreateResumeRequestDto requestDto, MultipartFile multipartFile);
}
