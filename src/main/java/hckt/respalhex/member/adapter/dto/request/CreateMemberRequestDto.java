package hckt.respalhex.member.adapter.dto.request;

import hckt.respalhex.global.dto.RequestDto;
import hckt.respalhex.member.application.dto.request.PostMemberRequestDto;
import lombok.Builder;

@Builder
public record CreateMemberRequestDto(
        String email,
        String password,
        String nickname,
        String picture,
        String provider
) implements RequestDto<PostMemberRequestDto> {
    @Override
    public PostMemberRequestDto convertToApplicationDto() {
        return new PostMemberRequestDto(email, password, nickname, picture, provider);
    }
}
