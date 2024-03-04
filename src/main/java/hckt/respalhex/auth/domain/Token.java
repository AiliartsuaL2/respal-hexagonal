package hckt.respalhex.auth.domain;

import lombok.Getter;

public record Token(
        @Getter
        String accessToken,
        @Getter
        String refreshToken
){
}
