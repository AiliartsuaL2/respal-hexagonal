package hckt.respalhex.auth.domain;

public record Token(
        String accessToken,
        String refreshToken
){
}
