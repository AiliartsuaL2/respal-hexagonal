package hckt.respalhex.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    NOT_EXIST_MEMBER_EXCEPTION(HttpStatus.BAD_REQUEST, "회원이 존재하지 않아요"),
    NOT_EXIST_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "email이 존재하지 않아요");

    private final HttpStatus httpStatus;
    private final String message;
}
