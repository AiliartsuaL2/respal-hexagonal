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
        validate(email, ErrorMessage.NOT_EXIST_EMAIL_EXCEPTION);
        validate(password, ErrorMessage.NOT_EXIST_PASSWORD_EXCEPTION);
        validate(nickname, ErrorMessage.NOT_EXIST_NICKNAME_EXCEPTION);
        validate(provider, ErrorMessage.NOT_EXIST_PROVIDER_TYPE_EXCEPTION);
    }
}
