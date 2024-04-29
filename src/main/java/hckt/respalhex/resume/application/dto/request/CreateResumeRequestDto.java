package hckt.respalhex.resume.application.dto.request;

import hckt.respalhex.global.dto.ApplicationRequestDto;
import hckt.respalhex.resume.exception.ErrorMessage;

public record CreateResumeRequestDto(String title, Long memberId) implements ApplicationRequestDto {
    public CreateResumeRequestDto {
        requiredArgumentValidation(title, ErrorMessage.NOT_EXIST_TITLE_EXCEPTION.getMessage());
        requiredArgumentValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID_EXCEPTION.getMessage());
    }
}
