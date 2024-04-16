package hckt.respalhex.resume.application.port.out;

import hckt.respalhex.resume.domain.ResumeFile;

public interface CommandResumeFilePort {
    void create(ResumeFile resumeFile);
}
