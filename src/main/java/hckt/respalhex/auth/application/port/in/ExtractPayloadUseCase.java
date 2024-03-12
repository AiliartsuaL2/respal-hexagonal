package hckt.respalhex.auth.application.port.in;

public interface ExtractPayloadUseCase {
    Long extractPayload(String accessToken);
}
