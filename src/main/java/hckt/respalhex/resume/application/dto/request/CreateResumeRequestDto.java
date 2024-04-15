package hckt.respalhex.resume.application.dto.request;

import hckt.respalhex.global.dto.ApplicationRequestDto;

public record CreateResumeRequestDto(String title, String filePath, Long memberId) implements ApplicationRequestDto {

}
