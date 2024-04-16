package hckt.respalhex.resume.adapter.out.persistence;

import hckt.respalhex.global.annotation.Adapter;
import hckt.respalhex.resume.application.port.out.CommandResumeFilePort;
import hckt.respalhex.resume.application.port.out.LoadResumeFilePort;
import hckt.respalhex.resume.domain.ResumeFile;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adapter
@RequiredArgsConstructor
public class ResumeFilePersistenceAdapter implements CommandResumeFilePort, LoadResumeFilePort {
    private final ResumeFileRepository resumeFileRepository;

    @Override
    public void create(ResumeFile resumeFile) {
        resumeFileRepository.save(resumeFile);
    }

    @Override
    public List<ResumeFile> findResumeFilesByResumeId(Long resumeId) {
        return resumeFileRepository.findResumeFilesByResumeId(resumeId);
    }
}
