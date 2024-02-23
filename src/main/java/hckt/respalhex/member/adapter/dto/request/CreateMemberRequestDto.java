package hckt.respalhex.member.adapter.dto.request;

import hckt.respalhex.global.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import org.springframework.util.ObjectUtils;

public record CreateMemberRequestDto(String email, String password, String nickname, String picture) implements RequestDto<PostMemberRequestDto> {
    @Override
    public PostMemberRequestDto convertToApplicationDto() {
        validate();
        return new PostMemberRequestDto(email, password, nickname, picture);
    }

    private void validate() {
        if(ObjectUtils.isEmpty(this.email)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_EMAIL_EXCEPTION.getMessage());
        }
    }
}
