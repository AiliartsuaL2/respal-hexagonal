package hckt.respalhex.resume.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    PERMISSION_DENIED_TO_DELETE(HttpStatus.FORBIDDEN, "삭제 권한이 없어요"),
    PERMISSION_DENIED_TO_UPDATE(HttpStatus.FORBIDDEN, "수정 권한이 없어요"),
    NOT_EXIST_RESUME_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 이력서가 존재하지 않아요"),
    NOT_EXIST_RESUME_ID_EXCEPTION(HttpStatus.BAD_REQUEST, "이력서 ID가 존재하지 않아요"),
    NOT_EXIST_MEMBER_ID_EXCEPTION(HttpStatus.BAD_REQUEST, "회원 ID가 존재하지 않아요"),
    NOT_EXIST_TITLE_EXCEPTION(HttpStatus.BAD_REQUEST, "제목이 존재하지 않아요"),
    NOT_EXIST_ORIGIN_NAME_EXCEPTION(HttpStatus.BAD_REQUEST, "원본 파일명이 존재하지 않아요"),
    NOT_EXIST_REGISTERED_NAME_EXCEPTION(HttpStatus.BAD_REQUEST, "저장소 파일명이 존재하지 않아요"),
    NOT_EXIST_ACCESS_URL_EXCEPTION(HttpStatus.BAD_REQUEST, "접근 Url이 존재하지 않아요"),
    NOT_EXIST_EXTENSION_EXCEPTION(HttpStatus.BAD_REQUEST, "파일 확장자가 존재하지 않아요"),
    REGISTER_MULTIPART_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장중 오류가 발생했어요");

    private final HttpStatus httpStatus;
    private final String message;
}
