package hckt.respalhex.member.exception;

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
    ALREADY_EXIST_MEMBER_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입된 이메일이에요"),
    NOT_EXIST_PROVIDER_TYPE_EXCEPTION(HttpStatus.BAD_REQUEST, "존재하지 않는 소셜 로그인 타입이에요"),
    NOT_EXIST_OAUTH_INFO_EXCEPTION(HttpStatus.BAD_REQUEST, "OAuth 정보가 존재하지 않습니다."),
    NOT_MATCH_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않아요"),
    COMMUNICATE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 서버와 통신중 에러가 발생했어요"),
    NOT_EXIST_MEMBER_ID_EXCEPTION(HttpStatus.BAD_REQUEST, "회원 ID가 존재하지 않아요"),
    NOT_EXIST_CLIENT_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 client가 존재하지 않아요");

    private final HttpStatus httpStatus;
    private final String message;
}
