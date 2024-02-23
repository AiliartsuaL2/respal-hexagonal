package hckt.respalhex.member.adapter.dto.request;

import hckt.respalhex.global.exception.ErrorMessage;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import lombok.Builder;
import org.springframework.util.ObjectUtils;

@Builder
public record CreateMemberRequestDto(
        String email,
        String password,
        String nickname,
        String picture
) implements RequestDto<PostMemberRequestDto> {
    @Override
    public PostMemberRequestDto convertToApplicationDto() {
        return new PostMemberRequestDto(email, password, nickname, picture);
    }
}
