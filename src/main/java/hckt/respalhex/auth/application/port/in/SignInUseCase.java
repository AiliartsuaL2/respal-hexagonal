package hckt.respalhex.auth.application.port.in;

import hckt.respalhex.auth.application.dto.request.LogInRequestDto;
import hckt.respalhex.auth.application.dto.response.LogInResponseDto;

public interface SignInUseCase {
    LogInResponseDto signIn(LogInRequestDto requestDto);
}
