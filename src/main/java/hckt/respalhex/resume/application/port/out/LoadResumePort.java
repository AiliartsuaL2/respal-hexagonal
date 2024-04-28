package hckt.respalhex.resume.application.port.out;

import hckt.respalhex.resume.domain.Resume;

import java.util.Optional;

public interface LoadResumePort {
    Optional<Resume> findById(Long resumeId);

    Optional<Resume> findResumeWithMemberInfo(Long resumeId);
}