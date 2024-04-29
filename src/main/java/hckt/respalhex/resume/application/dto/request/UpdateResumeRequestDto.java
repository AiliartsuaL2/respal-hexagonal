package hckt.respalhex.resume.application.dto.request;

import hckt.respalhex.global.dto.ApplicationRequestDto;
import hckt.respalhex.resume.exception.ErrorMessage;

public record UpdateResumeRequestDto(Long resumeId, String title, Long memberId) implements ApplicationRequestDto {
    public UpdateResumeRequestDto {
        requiredArgumentValidation(resumeId, ErrorMessage.NOT_EXIST_RESUME_ID_EXCEPTION.getMessage());
        requiredArgumentValidation(title, ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
        requiredArgumentValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
    }
}
