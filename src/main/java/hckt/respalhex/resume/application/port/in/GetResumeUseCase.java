package hckt.respalhex.resume.application.port.in;

import hckt.respalhex.resume.application.dto.response.GetResumeResponseDto;

public interface GetResumeUseCase {
    GetResumeResponseDto view(Long resumeId, Long memberId);
}
