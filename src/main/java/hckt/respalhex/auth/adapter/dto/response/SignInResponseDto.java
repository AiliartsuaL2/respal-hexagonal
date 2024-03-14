package hckt.respalhex.auth.adapter.dto.response;

public record SignInResponseDto(
        String accessToken,
        String refreshToken
) {
}
