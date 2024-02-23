package hckt.respalhex.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorResponse {
    private final boolean success;
    private final Error error;

    @Getter
    private static class Error {
        private Integer errorCode;
        private String message;
    }

    public ApiErrorResponse (HttpStatus httpStatus, String message) {
        Error error = new Error();
        error.errorCode = httpStatus.value();
        error.message = message;

        this.success = false;
        this.error = error;
    }
}
