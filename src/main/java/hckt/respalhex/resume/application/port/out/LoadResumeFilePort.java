package hckt.respalhex.resume.application.port.out;

import hckt.respalhex.resume.domain.ResumeFile;

import java.util.List;

public interface LoadResumeFilePort {
    List<ResumeFile> findResumeFilesByResumeId(Long resumeId);
}
