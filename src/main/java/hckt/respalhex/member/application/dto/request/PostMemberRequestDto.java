package hckt.respalhex.member.application.dto.request;

import hckt.respalhex.global.exception.ErrorMessage;
import lombok.Builder;
import org.springframework.util.ObjectUtils;

@Builder
public record PostMemberRequestDto(
        String email,
        String password,
        String nickname,
        String picture,
        String provider
) {
    public PostMemberRequestDto {
        validate(email, ErrorMessage.NOT_EXIST_EMAIL_EXCEPTION);
        validate(password, ErrorMessage.NOT_EXIST_PASSWORD_EXCEPTION);
        validate(nickname, ErrorMessage.NOT_EXIST_NICKNAME_EXCEPTION);
        validate(provider, ErrorMessage.NOT_EXIST_PROVIDER_TYPE_EXCEPTION);
    }

    private void validate(Object data, ErrorMessage errorMessage) {
        if(ObjectUtils.isEmpty(data)) {
            throw new IllegalArgumentException(errorMessage.getMessage());
        }
    }
}
