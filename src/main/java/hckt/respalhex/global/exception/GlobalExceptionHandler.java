package hckt.respalhex.global.exception;

import hckt.respalhex.global.dto.ApiErrorResponse;
import hckt.respalhex.member.exception.CommunicationException;
import hckt.respalhex.resume.exception.MultipartException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class : {}, StatusCode : {}, Message : {}";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> illegalArgumentException(IllegalArgumentException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn(
                LOG_FORMAT,
                ex.getClass().getSimpleName(),
                status.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(status)
                .body(new ApiErrorResponse(status, ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> illegalStateException(IllegalStateException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn(
                LOG_FORMAT,
                ex.getClass().getSimpleName(),
                status.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(status)
                .body(new ApiErrorResponse(status, ex.getMessage()));
    }

    @ExceptionHandler(CommunicationException.class)
    public ResponseEntity<ApiErrorResponse> communicationException(CommunicationException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.warn(
                LOG_FORMAT,
                ex.getClass().getSimpleName(),
                status.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(status)
                .body(new ApiErrorResponse(status, ex.getMessage()));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiErrorResponse> multipartException(MultipartException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.warn(
                LOG_FORMAT,
                ex.getClass().getSimpleName(),
                status.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(status)
                .body(new ApiErrorResponse(status, ex.getMessage()));
    }
}
