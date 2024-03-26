package hckt.respalhex.member.application.dto.request;

import hckt.respalhex.member.exception.ErrorMessage;
import org.springframework.util.ObjectUtils;

public interface ApplicationRequestDto {
    default <T> void validate(T data, ErrorMessage errorMessage) {
        if(ObjectUtils.isEmpty(data)) {
            throw new IllegalArgumentException(errorMessage.getMessage());
        }
    }
}
