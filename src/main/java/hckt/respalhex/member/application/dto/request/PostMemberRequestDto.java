package hckt.respalhex.member.application.dto.request;

import hckt.respalhex.global.dto.ApplicationRequestDto;
import hckt.respalhex.member.exception.ErrorMessage;
import lombok.Builder;

@Builder
public record PostMemberRequestDto (
        String email,
        String password,
        String nickname,
        String picture,
        String provider
) implements ApplicationRequestDto {
    public PostMemberRequestDto {
        requiredArgumentValidation(email, ErrorMessage.NOT_EXIST_EMAIL_EXCEPTION.getMessage());
        requiredArgumentValidation(password, ErrorMessage.NOT_EXIST_PASSWORD_EXCEPTION.getMessage());
        requiredArgumentValidation(nickname, ErrorMessage.NOT_EXIST_NICKNAME_EXCEPTION.getMessage());
        requiredArgumentValidation(provider, ErrorMessage.NOT_EXIST_PROVIDER_TYPE_EXCEPTION.getMessage());
    }
}
