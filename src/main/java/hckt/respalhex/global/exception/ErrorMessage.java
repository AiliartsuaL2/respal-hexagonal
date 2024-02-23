package hckt.respalhex.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    NOT_EXIST_MEMBER_EXCEPTION(HttpStatus.BAD_REQUEST, "회원이 존재하지 않아요"),
    NOT_EXIST_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이메일이 존재하지 않아요"),
    NOT_EXIST_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 존재하지 않아요"),
    NOT_EXIST_NICKNAME_EXCEPTION(HttpStatus.BAD_REQUEST, "닉네임이 존재하지 않아요"),
    ALREADY_EXIST_MEMBER_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 이에요");

    private final HttpStatus httpStatus;
    private final String message;
}
