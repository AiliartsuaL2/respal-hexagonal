package hckt.respalhex.resume.adapter.out.persistence;

import hckt.respalhex.global.annotation.Adapter;
import hckt.respalhex.resume.application.port.out.CommandResumePort;
import hckt.respalhex.resume.application.port.out.LoadResumePort;
import hckt.respalhex.resume.domain.Resume;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class ResumePersistenceAdapter implements CommandResumePort, LoadResumePort {
    private final ResumeRepository resumeRepository;

    @Override
    public void create(Resume resume) {
        resumeRepository.save(resume);
    }

    @Override
    public Optional<Resume> findById(Long resumeId) {
        return resumeRepository.findById(resumeId);
    }

    @Override
    public Optional<Resume> findResumeWithMemberInfo(Long resumeId) {
        return resumeRepository.findResumeWithMemberInfo(resumeId);
    }
}
