package hckt.respalhex.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 이에요"),
    NOT_EXIST_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "존재하지 않는 Refresh Token 이에요"),
    NOT_EXIST_ACCESS_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "존재하지 않는 Access Token 이에요"),
    NOT_EXIST_MEMBER_ID_EXCEPTION(HttpStatus.FORBIDDEN, "회원 ID가 존재하지 않아요");

    private final HttpStatus httpStatus;
    private final String message;
}
