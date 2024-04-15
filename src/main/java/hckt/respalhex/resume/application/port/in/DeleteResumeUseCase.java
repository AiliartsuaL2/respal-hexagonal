package hckt.respalhex.resume.application.port.in;

public interface DeleteResumeUseCase {
    void delete(Long resumeId, Long memberId);
}
