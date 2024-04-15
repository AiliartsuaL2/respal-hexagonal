package hckt.respalhex.resume.application.port.in;

import hckt.respalhex.resume.application.dto.request.UpdateResumeRequestDto;

public interface UpdateResumeUseCase {
    void update(UpdateResumeRequestDto requestDto);
}
