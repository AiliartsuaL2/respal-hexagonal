package hckt.respalhex.resume.application.port.out;

import hckt.respalhex.resume.domain.Resume;
import hckt.respalhex.resume.domain.ResumeFile;

import java.util.List;
import java.util.Optional;

public interface LoadResumePort {
    Optional<Resume> findById(Long resumeId);

    List<ResumeFile> findResumeFilesByResumeId(Long resumeId);

    Optional<Resume> findResumeWithMemberInfo(Long resumeId);
}
