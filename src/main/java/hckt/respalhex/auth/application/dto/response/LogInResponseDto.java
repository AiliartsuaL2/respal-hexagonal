package hckt.respalhex.auth.application.dto.response;

import hckt.respalhex.auth.domain.Token;

public record LogInResponseDto(String accessToken, String refreshToken) {
    public static LogInResponseDto create(Token token) {
        return new LogInResponseDto(token.accessToken(), token.refreshToken());
    }
}
