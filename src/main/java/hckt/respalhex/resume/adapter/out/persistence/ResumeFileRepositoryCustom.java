package hckt.respalhex.resume.adapter.out.persistence;

import hckt.respalhex.resume.domain.ResumeFile;

import java.util.List;

public interface ResumeFileRepositoryCustom {
    List<ResumeFile> findResumeFilesByResumeId(Long resumeId);
}
