package hckt.respalhex.resume.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    PERMISSION_DENIED_TO_DELETE(HttpStatus.FORBIDDEN, "삭제 권한이 없어요"),
    PERMISSION_DENIED_TO_UPDATE(HttpStatus.FORBIDDEN, "수정 권한이 없어요"),
    NOT_EXIST_RESUME_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 이력서가 존재하지 않아요");

    private final HttpStatus httpStatus;
    private final String message;
}
