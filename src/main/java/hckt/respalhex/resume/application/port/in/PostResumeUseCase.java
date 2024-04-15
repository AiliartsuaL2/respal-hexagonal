package hckt.respalhex.resume.application.port.in;

import hckt.respalhex.resume.application.dto.request.CreateResumeRequestDto;

public interface PostResumeUseCase {
    void create(CreateResumeRequestDto requestDto);
}
