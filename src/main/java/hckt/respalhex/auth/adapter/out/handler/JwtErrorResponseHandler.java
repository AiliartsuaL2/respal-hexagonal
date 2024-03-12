package hckt.respalhex.auth.adapter.out.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.global.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtErrorResponseHandler {
    private final ObjectMapper objectMapper;

    public void generateJwtErrorResponse(HttpServletResponse response, ErrorMessage errorMessage) throws IOException {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(errorMessage.getHttpStatus(), errorMessage.getMessage());
        response.setStatus(errorMessage.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}
