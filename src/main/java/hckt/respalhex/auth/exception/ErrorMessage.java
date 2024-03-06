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
    NOT_EXIST_MEMBER_ID_EXCEPTION(HttpStatus.FORBIDDEN, "회원 ID가 존재하지 않아요"),
    SIGNATURE_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "잘못된 토큰 정보에요"),
    MALFORMED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰 형식이에요"),
    EXPIRED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰 정보에요"),
    INCORRECT_REFRESH_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "일치하지 않는 Refresh Token 이에요"),
    PERMISSION_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "접근 권한이 없어요"),
    NOT_EXIST_TOKEN_INFO_EXCEPTION(HttpStatus.FORBIDDEN, "토큰 정보가 존재하지 않아요");

    private final HttpStatus httpStatus;
    private final String message;
}
