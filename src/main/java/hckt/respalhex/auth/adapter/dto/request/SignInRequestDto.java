package hckt.respalhex.auth.adapter.dto.request;

import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.global.dto.RequestDto;

public record SignInRequestDto(
        String email,
        String password
) implements RequestDto<LogInRequestDto> {
    @Override
    public LogInRequestDto convertToApplicationDto() {
        return new LogInRequestDto(this.email, this.password);
    }
}
