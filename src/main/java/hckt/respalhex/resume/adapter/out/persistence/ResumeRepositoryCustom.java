package hckt.respalhex.resume.adapter.out.persistence;

import hckt.respalhex.resume.domain.Resume;

import java.util.Optional;

public interface ResumeRepositoryCustom {
    Optional<Resume> findResumeWithMemberInfo(Long resumeId);
}
