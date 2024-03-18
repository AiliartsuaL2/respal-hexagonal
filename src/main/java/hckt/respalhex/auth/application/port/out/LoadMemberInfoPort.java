package hckt.respalhex.auth.application.port.out;

import hckt.respalhex.auth.application.dto.request.LogInRequestDto;

import java.util.concurrent.TimeoutException;

public interface LoadMemberInfoPort {
    Long signIn(LogInRequestDto requestDto) throws TimeoutException;
}
